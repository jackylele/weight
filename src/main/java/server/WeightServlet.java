package server;

import com.alibaba.fastjson.JSONObject;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WeightServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final JSONObject json = new JSONObject();

    public WeightServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json; charset=utf-8");

        // 设置：Access-Control-Allow-Origin头，处理Session问题
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("P3P", "CP=CAO PSA OUR");
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
            response.addHeader("Access-Control-Max-Age", "120");
        }
        PrintWriter out = response.getWriter();
        try {
            out.println(GetWeight.json);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static JSONObject getWeight() throws IllegalAccessException, NativeException {
//        JSONObject json = new JSONObject();
        System.load("C:\\iSmartSystem\\pos_ad_dll.dll");

        JNative read_baseinfo = new JNative("pos_ad_dll", "read_baseinfo");
        Pointer pBuf = new Pointer(MemoryBlockFactory.createMemoryBlock(65));
        //read_baseinfo
        read_baseinfo.setRetVal(Type.INT);

        int i = 0;
        read_baseinfo.setParameter(i++, pBuf);
        read_baseinfo.invoke();

        int result = read_baseinfo.getRetValAsInt();

        if (result == 240) {
            String baseinfo = pBuf.getAsString();
        }


        pBuf.dispose();
//---------------------------------------------------------------------------------------、
        
        try {
    			Thread.currentThread().sleep(1000);
    		} catch (InterruptedException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
            
        Pointer r = new Pointer(MemoryBlockFactory.createMemoryBlock(18));
        JNative read_standard = new JNative("pos_ad_dll", "read_standard");
        read_standard.setRetVal(Type.INT);
        i = 0;
        read_standard.setParameter(i++, r);

        while (true) {

            try {
                read_standard.invoke();
                result = read_standard.getRetValAsInt();

                if (result == 240) { //正常

                    int WeightStatus = -1;
                    String weightValue = "---";
                    String tareValue = "---";
                    WeightStatus = (int) (r.getAsByte(0));
                    r.setByteAt(0, (byte) 30);

                    String ret = r.getAsString();

                    if (ret.length() > 0) {
                        ret = r.getAsString().substring(1);

                        String[] wArray = ret.split("P");
                        weightValue = wArray[0];//净重

                        if (wArray.length > 1) {
                            tareValue = wArray[1];//皮重
                        }
                        json.put("weight", weightValue);
                    }

                    if (WeightStatus == 0) { //无皮重 非零位 非稳定
                    } else if (WeightStatus == 1) { //无皮重 非零位 稳定
                    } else if (WeightStatus == 2) { //无皮重 零位 非稳定
                    } else if (WeightStatus == 3) { //无皮重 零位 稳定
                    } else if (WeightStatus == 4) { //有皮重 非零位 非稳定
                    } else if (WeightStatus == 5) { //有皮重 非零位 稳定
                    } else if (WeightStatus == 6) { //有皮重 零位 非稳定
                    } else if (WeightStatus == 7) { //有皮重 零位 稳定
                    } else { //
                    }
                }
               else { //获取失败
                }
            } catch (Exception e) {//异常
                e.printStackTrace();
            }
//            return json;
        }
    }
}  