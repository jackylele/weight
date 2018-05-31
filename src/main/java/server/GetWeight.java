package server;
import com.alibaba.fastjson.JSONObject;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;


public class GetWeight extends Thread{

    public static final JSONObject json = new JSONObject();

    @Override
    public void run(){
        try {
            getWeight();
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
