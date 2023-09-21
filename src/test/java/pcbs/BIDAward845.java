package pcbs;

public class BIDAward845 {
	
	public static String [] extneralBidAward = {
		"BidAwardType",
		"ContractRefNum",
		"ContractType",
		"CustRealmNum",
		"StartDate",
		"EndDate",
		"PubDate",
		"RunDate",
		"PubRefNum",
		"RefNum"
		
	};
	
	public static String [] externalBidAwardCustomer = {
			"ChangeType",
			"StartDate",
			"EndDate",
			"CustomerRefNum",
			"CustomerRefType",
			"CustomerName",
			"Street1",
			"City",
			"State",
			"ZipCode",
			"Code340b1"
	};
	
	public static String [] externalBidAwardProduct = {
			
			"ChangeType", // INITIAL
			"StartDate", // date format is 2023-01-01 00:00:00
			"EndDate", 
			"ProductNum",
			"ProductName",
			"UOM", //EACH
			"Price" // number format is $1,702.71
	};
	
	public static String [] externalBidAwardWholesaler = {
			"ChangeType", // INITIAL
			"Edi845Enabled", // true/false
			"EndDate", // date format 2023-12-31 23:59:59
			"StartDate",
			"WholesalerName",
			"WholesalerRefNum",
			"WholesalerRefType"
	};

}
