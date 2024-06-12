package eg.com.vodafone.model.enums;

/**
 * Created with IntelliJ IDEA.
 * Author : basma.alkerm
 * Date   : 4/7/13
 * Time   : 9:17 AM
 */
public enum XmlDataCollectionConverters {

    VPNXMLCountersConverter(1,"VPNXMLCountersConverter","VPNXMLCountersConverter"),
    VPNPlatformMeasures(2,"VPNPlatformMeasures","VPNPlatformMeasures"),
    VPNPMFStatisticsConverter(3,"VPNPMFStatisticsConverter","VPNPMFStatisticsConverter"),
    VPNXMLCompaniesConverter(4,"VPNXMLCompaniesConverter","VPNXMLCompaniesConverter"),
    CCNCountersGeneralConverter(5,"CCNCountersGeneralConverter","CCNCountersGeneralConverter"),
    CCNVoiceChargingCountersConverter(6,"CCNVoiceChargingCountersConverter","CCNVoiceChargingCountersConverter"),
    CCNPlatformMeasuresConverter(7,"CCNPlatformMeasuresConverter","CCNPlatformMeasuresConverter");


    private int converterId;
    private String converterName;
    private String converterClass;

    private XmlDataCollectionConverters(int id, String name, String className){
        this.converterId = id;
        this.converterName = name;
        this.converterClass = className;
    }

    public int getConverterId() {
        return converterId;
    }

    public String getConverterName() {
        return converterName;
    }

    public String getConverterClass() {
        return converterClass;
    }

    public static String getConverterClass(int id){
        for(XmlDataCollectionConverters converter:XmlDataCollectionConverters.values()){
            if(converter.getConverterId() == id){
                return converter.getConverterClass();
            }
        }
        return null;
    }
}
