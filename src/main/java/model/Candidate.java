package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    private String name;
    private String team;
    private int voteCount;
    private boolean tied;

    @Override
    public String toString() {
        return String.format("Candidate: %s, Team: %s, Vote count: %d", name, team, voteCount);
    }

    @Override
    public boolean equals(Object o) {
        // If candidate is compared with itself then return true
        if (o == this) {
            return true;
        }

        // Check if o is an instance of Candidate
        if (!(o instanceof Candidate candidate)) {
            return false;
        }

        // Compare the data members and return accordingly
        return name.equals(candidate.getName()) && team.equals(candidate.getTeam()) && voteCount == candidate.getVoteCount();
    }
}
