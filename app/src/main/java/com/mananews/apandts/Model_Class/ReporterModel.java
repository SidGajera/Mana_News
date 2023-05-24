package com.mananews.apandts.Model_Class;

import java.util.List;

public class ReporterModel {

    private String message, success;

    List<dataModel> data;

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public List<dataModel> getData() {
        return data;
    }

    public void setData(List<dataModel> data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", success = "+success+", message = "+message+"]";
    }

    public class dataModel
    {
        private String cat_id;

        private String reporter_id;

        private String description;

        private String thambnail;

        private String tag;

        private String media;

        private String long_description;

        private String title;

        private String type;

        public String getCat_id ()
        {
            return cat_id;
        }

        public void setCat_id (String cat_id)
        {
            this.cat_id = cat_id;
        }

        public String getReporter_id ()
        {
            return reporter_id;
        }

        public void setReporter_id (String reporter_id)
        {
            this.reporter_id = reporter_id;
        }

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getThambnail ()
        {
            return thambnail;
        }

        public void setThambnail (String thambnail)
        {
            this.thambnail = thambnail;
        }

        public String getTag ()
        {
            return tag;
        }

        public void setTag (String tag)
        {
            this.tag = tag;
        }

        public String getMedia ()
        {
            return media;
        }

        public void setMedia (String media)
        {
            this.media = media;
        }

        public String getLong_description ()
        {
            return long_description;
        }

        public void setLong_description (String long_description)
        {
            this.long_description = long_description;
        }

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [cat_id = "+cat_id+", reporter_id = "+reporter_id+", description = "+description+", thambnail = "+thambnail+", tag = "+tag+", media = "+media+", long_description = "+long_description+", title = "+title+", type = "+type+"]";
        }
    }


}


