TODO:

-Page for simple execution of methods (Use the async ones and return info to a text field somehow) --> jquery and REST?
	
Used Resources:
	Quartz integration: - http://www.opencodez.com/java/quartz-scheduler-with-spring-boot.htm
						- https://github.com/pavansolapure/opencodez-samples/tree/master/quartz-demo	
TODO QUARTZ:
	Bekijken of we group name als userID kunnen zetten om opvragen van jobs makkelijker te maken
	UserJob verder uitwerken. 
	
As per Best Practices of JobDataMap, we should not put complex objects in JobDataMap to avoid short term or long term issues. (There might be issues of serialization which of course will not be solved by modifying Complex object class to implement Serializable.)

Workaround: Pass complex objects as a Json String and deserialize them after retrieving it from JobDataMap.

For example,

class OrderLine {
        private long orderLineId;
        private Item item;
    }
    class Item {
        private long itemId;
        private String itemName;
   }
//Putting OrderLine object in JobDataMap
    jobDetail.getJobDataMap().put("complexData", new Gson().toJson(new OrderLine()));
// Retrieving data from JobDataMap
    String complexDataString = 
        context.getJobDetail().getJobDataMap().getString("complexData");
    OrderLine orderLine = new Gson().fromJson(complexDataString, OrderLine.class);
    
TODO: Exception handling for all twitterExceptions (check what exact kind of error -> end, retry, log, ....)

2017-10-13 09:51:42.388 WARN  [qtp1878169648-16] DefaultHandlerExceptionResolver - Failed to convert request element: org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException: Failed to convert value of type 'java.lang.String' to required type 'net.nilsghesquiere.valueobjects.ExtendedJobInfo'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'net.nilsghesquiere.valueobjects.ExtendedJobInfo': no matching editors or conversion strategy found