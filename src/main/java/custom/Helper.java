package custom;

public class Helper
{

    public static double slopeToDegree(double slope){
        return  Math.toDegrees(Math.atan(slope));
    }

    public static  double degreeToSlope(double degree){
        double rad = Math.toRadians(degree);
        return Math.tan(rad);
    }

}
