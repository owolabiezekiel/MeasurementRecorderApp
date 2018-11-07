package owolabi.tobiloba.measurementrecorder.model;

public class Measurement {
    public String title;
    public String name;
    public int gender;
    public int head;
    public int neck;
    public int neckline;
    public int bustpoint;
    public int underbust;
    public int bust;
    public int waist;
    public int hip;
    public int shoulder;
    public int chest;
    public int gownlength;
    public int blouselength;
    public int shortGownLength;
    public int sleeveLength;
    public int armHole;
    public int kneeLength;
    public int halfLength;
    public int trouserLength;
    public int thigh;
    public int trouserBottom;


    public Measurement(){

    }

    public Measurement(String title, String name, int gender, int head, int neck, int neckline, int bustpoint, int underbust, int bust,
                       int waist, int hip, int shoulder, int chest, int gownlength, int blouselength, int shortGownLength, int sleeveLength,
                       int armHole, int kneeLength, int halfLength, int trouserLength, int thigh, int trouserBottom) {
        this.title = title;
        this.name = name;
        this.gender = gender;
        this.head = head;
        this.neck = neck;
        this.neckline = neckline;
        this.bustpoint = bustpoint;
        this.underbust = underbust;
        this.bust = bust;
        this.waist = waist;
        this.hip = hip;
        this.shoulder = shoulder;
        this.chest = chest;
        this.gownlength = gownlength;
        this.blouselength = blouselength;
        this.shortGownLength = shortGownLength;
        this.sleeveLength = sleeveLength;
        this.armHole = armHole;
        this.kneeLength = kneeLength;
        this.halfLength = halfLength;
        this.trouserLength = trouserLength;
        this.thigh = thigh;
        this.trouserBottom = trouserBottom;
    }
}
