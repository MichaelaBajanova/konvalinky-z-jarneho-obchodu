package roihunter.task.trello_resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Michaela Bajanova (469166)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrelloChecklistItem implements TrelloResource {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
