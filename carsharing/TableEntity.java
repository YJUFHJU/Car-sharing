package carsharing;

public class TableEntity {
    private Integer id;
    private String name;

    public TableEntity(Integer id, String name) {
        if (name.length() == 0)
            name = null;

        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer newId) {
        id = newId;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
}
