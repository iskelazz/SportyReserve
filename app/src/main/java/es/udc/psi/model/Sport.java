package es.udc.psi.model;

public class Sport {
    String Name;
    int MaxParticipants;

    public Sport() {}

    public Sport(String Name, int MaxParticipants)
    {
        this.Name = Name;
        this.MaxParticipants = MaxParticipants;
    }

    public int getMaxParticipants() {
        return MaxParticipants;
    }

    public void setMaxParticipants(int MaxParticipants) {
        this.MaxParticipants = MaxParticipants;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
