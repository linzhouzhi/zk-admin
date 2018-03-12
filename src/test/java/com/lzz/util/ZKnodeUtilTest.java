package com.lzz.util;

import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * Created by lzz on 17/5/8.
 */
public class ZKnodeUtilTest {
    @Test
    public void testGetAllPath() throws Exception {
        String path = "/";
        JSONObject jsonObject = ZKnodeUtil.getAllPath(path);
        System.out.println( jsonObject );
    }
}
