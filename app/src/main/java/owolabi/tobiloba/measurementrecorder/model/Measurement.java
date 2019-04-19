package owolabi.tobiloba.measurementrecorder.model;

public class Measurement {
    public String name;
    public int gender;
    public float head;
    public float neck;
    public float neckline;
    public float bustpoint;
    public float underbust;
    public float bust;
    public float waist;
    public float hip;
    public float shoulder;
    public float chest;
    public float gownlength;
    public float blouselength;
    public float shortGownLength;
    public float sleeveLength;
    public float armHole;
    public float kneeLength;
    public float halfLength;
    public float trouserLength;
    public float thigh;
    public float trouserBottom;


    public Measurement(){

    }

    public Measurement(String name, int gender, float head, float neck, float neckline, float bustpoint, float underbust, float bust, float waist,
                       float hip, float shoulder, float chest, float gownlength, float blouselength, float shortGownLength, float sleeveLength, float armHole, float kneeLength,
                       float halfLength, float trouserLength, float thigh, float trouserBottom) {
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
