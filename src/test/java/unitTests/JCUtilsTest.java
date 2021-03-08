package unitTests; /**
 * Created by koreny on 3/20/2017.
 */

import com.microfocus.jc.JC;
import com.microfocus.jc.plugins.Feature;
import com.microfocus.jc.plugins.FeatureFileAt;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

@Feature()
@FeatureFileAt()
public class JCUtilsTest {

    @Test
    public void testFindFeatureFileAtAnnotation() {
        Class<?> cls = JC.findCalledClassWithAnnotation(FeatureFileAt.class);
        Assert.assertNotNull(cls);
    }

    @Test
    public void testSimpleFindAnnotation() {
        Class<?> cls = JC.findCalledClassWithAnnotation(Feature.class);
        Assert.assertNotNull(cls);
    }

    @Test
    public void testFindAnnotationInsideAnnonimousFunc() {
        ArrayList<Class<?>> cls = new ArrayList<>();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                cls.add(JC.findCalledClassWithAnnotation(Feature.class));
            }
        };
        run.run();

        Assert.assertNotNull(cls.get(0));
    }

}
