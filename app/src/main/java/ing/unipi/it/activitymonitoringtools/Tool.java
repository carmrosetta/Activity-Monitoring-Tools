package ing.unipi.it.activitymonitoringtools;

/**
 * @brief Class that represents a tool, that is one of the modules that make up the application
 */

public class Tool {

    String name;


    /**
     * @brief Constructor
     * @param name String that represents the name of the tool
     */
    public Tool(String name) {
        this.name = name;
    }

    /**
     * @brief Method that gets the name of the tool
     * @return a String that represents the name of the tool
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Method that allows to set the name of the tool
     * @param name String that represents the name of the tool
     */
    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return this.name;
    }
}

