package appacitive;

import appacitive.callbacks.GetCallBack;
import appacitive.exceptions.AppacitiveException;
import appacitive.exceptions.ValidationError;
import appacitive.utilities.AppacitiveHttp;
import appacitive.utilities.Headers;
import appacitive.utilities.Urls;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley.
 */
public class AppacitiveObject extends AppacitiveEntity {
    public AppacitiveObject(Map<String, Object> entity) {
        super(entity);
        this.typeId = Long.parseLong(entity.get("__typeid").toString());
        this.type = entity.get("__type").toString();
    }

    protected Map<String, Object> GetMap()
    {
        Map<String, Object> nativeMap = super.GetMap();
        nativeMap.put("__type", this.type);
        nativeMap.put("__typeid", this.typeId);

        return nativeMap;
    }
    private String type = null;

    private long typeId = 0;

    public String getType() {
        return type;
    }

    public long getTypeId() {
        return typeId;
    }

    public void CreateInBackground() throws ValidationError
    {
//        if((type == null || this.type.isEmpty()) && (typeId == 0))
//        {
//            throw new ValidationError("Type and TypeId both cannot be empty while creating an object.");
//        }
//
//        final String url = Urls.ForObject.CreateObjectUrl(this.type);
//        final Map<String, String> headers = Headers.assemble();
//        final Map<String, Object> payload = this.GetMap();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    AppacitiveHttp.Put(url, headers, payload);
//                }
//                catch (AppacitiveException e)
//                {
//
//                }
//            }
//        })

    }

    public static void GetAsync(String type, long id, List<String> fields, GetCallBack<AppacitiveObject> callBack) throws ValidationError
    {
        if(type.isEmpty())
            throw new ValidationError("Type cannot be empty.");
        if(id <= 0)
            throw new ValidationError("Object id should be greater than equal to 0.");

        final String innerType = type;
        final long innerId = id;
        final List<String> innerFields = fields;
        final GetCallBack<AppacitiveObject> innerCallBack = callBack;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    AppacitiveObject obj = AppacitiveObject.Get(innerType, innerId, innerFields);
                    innerCallBack.Done(obj, null);
                }
                catch (AppacitiveException e)
                {
                    innerCallBack.Done(null, e);
                }
                catch (IOException e)
                {
                    innerCallBack.Done(null, new AppacitiveException(e.getMessage()));
                }
            }
        });
        t.start();
    }

    public static AppacitiveObject Get(String type, long id, List<String> fields) throws AppacitiveException, IOException
    {
        String url = Urls.ForObject.GetObjectUrl(type, id);
        Map<String, String> headers = Headers.assemble();
        Map<String, Object> response = AppacitiveHttp.Get(url, headers);
        return new AppacitiveObject((Map<String, Object>)response.get("object"));
    }


}
