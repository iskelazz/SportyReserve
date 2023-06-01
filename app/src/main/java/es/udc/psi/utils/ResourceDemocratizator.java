package es.udc.psi.utils;

import android.content.Context;
import android.content.res.Resources;

public class ResourceDemocratizator {


    private Context appContext;

    private static ResourceDemocratizator uniqueInstance;

    private ResourceDemocratizator(Context context) {
        appContext = context;
    }

    public static ResourceDemocratizator getInstance() {
        if (uniqueInstance == null)
            throw new NullPointerException("Please call initialize() before getting the instance.");
        return uniqueInstance;
    }

    public synchronized static void initialize(Context applicationContext) {
        if (applicationContext == null)
            throw new NullPointerException("Provided application context is null");
        else if (uniqueInstance == null) {
            uniqueInstance = new ResourceDemocratizator(applicationContext);
        }
    }
    /**
     * @exception android.content.res.Resources.NotFoundException
     * @param resId
     * @return String if there is a resource with that id, an exception will be launched
     */
    public String getStringFromResourceID(int resId) throws Resources.NotFoundException
    {
        return appContext.getString(resId);
    }
}
