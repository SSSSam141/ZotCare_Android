package com.aware.ui.esms;

import com.aware.ESM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by denzilferreira on 22/02/16.
 */
public class ESMFactory {

    private JSONArray queue;

    public ESMFactory() {
        this.queue = new JSONArray();
    }

    public JSONArray getQueue() {
        return this.queue;
    }

    public ESMFactory addESM(ESM_Question esm) {
        try {
            queue.put(esm.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ESMFactory removeESM(int position) {
        queue.remove(position);
        return this;
    }

    public String build() {
        return this.queue.toString();
    }

    public ESMFactory rebuild(JSONArray queue) throws JSONException {
        this.queue = queue;
        return this;
    }

    public ESM_Question getESM(int esmType, JSONObject esm, int _id) throws JSONException {
        String esm_title = esm.getString("esm_title");
        if(esm_title.contains("How many unique applications have you used") ||
                esm_title.contains("How many minutes have you used") ||
                esm_title.contains("How many times did you turn on the screen of your phone")) {
            esmType = 9;
            esm.put("esm_type", 9);
        }

        switch (esmType) {
            case ESM.TYPE_ESM_TEXT:
                return new ESM_Freetext().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_CHECKBOX:
                return new ESM_Checkbox().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_LIKERT:
                return new ESM_Likert().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_QUICK_ANSWERS:
                return new ESM_QuickAnswer().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_RADIO:
                return new ESM_Radio().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_SCALE:
                return new ESM_Scale().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_DATETIME:
                return new ESM_DateTime().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_PAM:
                return new ESM_PAM().rebuild(esm).setID(_id);
            case ESM.TYPE_ESM_NUMBER:
                return new ESM_Number().rebuild(esm).setID(_id);
            default:
                return null;
        }
    }
}
