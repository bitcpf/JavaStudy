import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * BaseballElimination
 */
public class BaseballElimination {

    // mapping of team names to indices in array
    private LinkedHashMap<String, Integer> names
            = new LinkedHashMap<String, Integer>();
    private Team[] teams;
    private Iterable<String>[] certificates;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        readTeams(filename);
        checkEliminations();
    }

    private void readTeams(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        teams = new Team[n];
        int k = 0;
        while (k < n && !in.isEmpty()) {
            String name = in.readString();
            int wins = in.readInt();
            int losses = in.readInt();
            int remaining = in.readInt();
            int[] games = new int[n];
            for (int i = 0; i < games.length; i++) {
                games[i] = in.readInt();
            }
            names.put(name, k);
            teams[k++] = new Team(name, wins, losses, remaining, games);
        }
    }

    private void checkEliminations() {
        certificates = new Iterable[teams.length];
        for (int i = 0; i < teams.length; i++) {
            checkElimination(i);
        }
    }

    private void checkElimination(int teamX) {
        if (checkTrivialElimination(teamX)) return;
        checkFullElimination(teamX);
    }

    private boolean checkTrivialElimination(int teamX) {
        int maxWins = teams[teamX].getWins() + teams[teamX].getRemaining();
        for (int j = 0; j < teams.length; j++) {
            if (j != teamX && teams[j].getWins() > maxWins) {
                certificates[teamX] = Collections.singletonList(teams[j].getName());
                return true;
            }
        }
        return false;
    }

    private void checkFullElimination(int teamX) {
        FlowNetwork network = constructFlowNetwork(teamX);
        computeFullElimination(network, teamX);
    }

    private FlowNetwork constructFlowNetwork(int teamX) {
        FlowNetwork network = new FlowNetwork(calculateVertexCount(teamX));

        // Add edges from teams vertices to t vertex
        int maxWinsX = teams[teamX].getWins() + teams[teamX].getRemaining();
        for (int i = 0; i < teams.length; i++) {
            // Edge from team i to t
            if (i != teamX) {
                network.addEdge(
                        new FlowEdge(i + 2, 1, maxWinsX - teams[i].getWins()));
            }
        }

        // current vertex
        int v = teams.length + 2;
        // Add edges between s, games, and teams
        for (int i = 0; i < teams.length - 1; i++) {
            if (i == teamX) continue;
            for (int j = i; j < teams.length; j++) {
                if (j == teamX) continue;
                // if games remaining
                if (teams[i].getGames()[j] != 0) {
                    // edge between s and game
                    network.addEdge(
                            new FlowEdge(0, v, teams[i].getGames()[j]));
                    // edges between game and teams
                    network.addEdge(
                            new FlowEdge(v, i + 2, Double.POSITIVE_INFINITY));
                    network.addEdge(
                            new FlowEdge(v, j + 2, Double.POSITIVE_INFINITY));
                    v++;
                }
            }
        }
        return network;
    }

    private int calculateVertexCount(int teamX) {
        // Indices: s - 0, t - 1,
        // teams - (2;teams.length+1), games - the rest of vertices.
        int count = 2 + teams.length;
        for (int i = 0; i < teams.length - 1; i++) {
            if (i == teamX) continue;
            for (int j = i; j < teams.length; j++) {
                if (j == teamX) continue;
                // if games remaining
                if (teams[i].getGames()[j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private void computeFullElimination(FlowNetwork network, int teamX) {
        FordFulkerson ff = new FordFulkerson(network, 0, 1);
        List<String> certificate = new ArrayList<String>();
        for (int i = 0; i < teams.length; i++) {
            if (i == teamX) continue;
            // if team vertex is in the min cut then
            // add it to elimination certificate.
            if (ff.inCut(i + 2)) {
                certificate.add(teams[i].getName());
            }
        }
        if (!certificate.isEmpty()) {
            certificates[teamX] = certificate;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.length;
    }

    // all teams
    public Iterable<String> teams() {
        return names.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        assertTeamExists(team);
        return teams[names.get(team)].getWins();
    }

    private void assertTeamExists(String team) {
        if (!names.containsKey(team)) {
            throw new IllegalArgumentException("No team found.");
        }
    }

    // number of losses for given team
    public int losses(String team) {
        assertTeamExists(team);
        return teams[names.get(team)].getLosses();
    }

    // number of remaining games for given team
    public int remaining(String team) {
        assertTeamExists(team);
        return teams[names.get(team)].getRemaining();
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        assertTeamExists(team1);
        assertTeamExists(team2);
        return teams[names.get(team1)].getGames()[names.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        assertTeamExists(team);
        return certificateOfElimination(team) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        assertTeamExists(team);
        return certificates[names.get(team)];
    }

    private static class Team {
        private String name;
        private int wins;
        private int losses;
        private int remaining;
        private int[] games;

        private Team(String name, int wins, int losses, int remaining, int[] games) {
            this.name = name;
            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
            this.games = games;
        }

        private String getName() {
            return name;
        }

        private int getWins() {
            return wins;
        }

        private int getLosses() {
            return losses;
        }

        private int getRemaining() {
            return remaining;
        }

        private int[] getGames() {
            return games;
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
