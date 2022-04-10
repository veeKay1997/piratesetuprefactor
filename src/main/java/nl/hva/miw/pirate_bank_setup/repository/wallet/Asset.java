package nl.hva.miw.pirate_bank_setup.repository.wallet;


import java.util.Objects;

public class Asset {
    private String name;
    private String abbreviation;

    public Asset(String asset, String abbreviation) {
        this.name = asset;
        this.abbreviation = abbreviation;
    }

    public Asset(String name) {
        this.name = name;
    }

    public Asset(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(name, asset.name) && Objects.equals(abbreviation, asset.abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, abbreviation);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "asset='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
