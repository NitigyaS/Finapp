import custom.Helper;
import org.junit.Assert;
import org.junit.Test;

public class HelperTest
{
    @Test
    public void DegreeToSlope(){
        Assert.assertEquals(0.83901,Helper.degreeToSlope(40),4);
        Assert.assertEquals(1.73205,Helper.degreeToSlope(60),4);
    }

    @Test
    public void SlopDegreeTest(){
        Assert.assertEquals(60, Math.round(Helper.slopeToDegree(1.73205)));
        Assert.assertEquals(40, Math.round(Helper.slopeToDegree(0.83901)));
    }

}
