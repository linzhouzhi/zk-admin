package util;

import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * Created by lzz on 17/5/8.
 */
public class ZKnodeUtilTest {
    @Test
    public void testGetAllPath() throws Exception {
        JSONObject jsonObject = ZKnodeUtil.getAllPath();
        System.out.println( jsonObject );
    }
}
