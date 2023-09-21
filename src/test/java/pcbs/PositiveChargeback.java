package pcbs;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PositiveChargeback {
	
	Map<String,String> pcbMap;

	public final static String [] positiveChargebackHeaders = { 
			"ExternalResubmissionType",
			"ExternalCurrency",
			"ExternalOrgId",
			"ExternalParentDistrBranchId",
			"ExternalParentDistrDebitMemo",
			"ExternalParentDistrDebitMemoDate",
			"ExternalParentDistrName",
			"ExternalBranchDistrBranchId",
			"ExternalBranchDistrRefIdType",
			"ExternalBranchDistrName",
			"ShipToCustomerNum",
			"ExternalShipToCustRefIdType",
			"ShipToExternalCustomer1",
			"ExternalShipToCust1RefIdType",
			"ShipToExternalCustomer2",
			"ExternalShipToCust2RefIdType",
			"ShipToExternalCustomer3",
			"ExternalShipToCust3RefIdType",
			"ShipToExternalCustomer4",
			"ExternalShipToCust4RefIdType",
			"ShipToCustomerName",
			"ShipToCustomerAddrLine1",
			"ShipToCustomerAddrLine2",
			"ShipToCustomerCity",
			"ShipToCustomerStBZe",
			"ShipToCustomerZip",
			"ShipToCustomerCountry",
			"SoldToCustomerNum",
			"ExternalSoldToCustRefIdType",
			"SoldToExternalCustomer1",
			"ExternalSoldToCust1RefIdType",
			"SoldToExternalCustomer2",
			"ExternalSoldToCust2RefIdType",
			"SoldToExternalCustomer3",
			"ExternalSoldToCust3RefIdType",
			"SoldToExternalCustomer4",
			"ExternalSoldToCust4RefIdType",
			"SoldToCustomerName",
			"SoldToCustomerAddrLine1",
			"SoldToCustomerAddrLine2",
			"SoldToCustomerCity",
			"SoldToCustomerStBZe",
			"SoldToCustomerZip",
			"SoldToCustomerCountry",
			"ExternalItemId",
			"ExternalInvoiceDate",
			"ExternalRefId",
			"ExternalLineRefId",
			"ResubmissionLineRefNum",
			"ExternalContractAmount",
			"ExternalOriginalContractId",
			"ExternalInvoiceQuantityAmount",
			"ExternalInvoiceUom",
			"ExternalDistrCostAmount",
			"ExternalTotalDistrRebBZeAmount",
			"ExternalLineLinkId",
			"ExtBundleDiscId",
			"ExternalCBZalogType",
			"ExternalLineType",
			"Accuracy",
			"ExternalBBZchNumber",
			"SerialNum",
			"FirstProductIdType",
			"ProdReferenceDescription",
			"ExternalParentDistrRefIdType",
			"ExternalComment"
	};
	
	
	private Map<String,String> positiveChargebackMap() {
		pcbMap = Arrays.asList(positiveChargebackHeaders)
			.stream()
			.map(e -> e)
			.collect(Collectors.toMap(e -> e, e -> ""));
		return pcbMap;
	}
	
	public Map<String,String> getPositiveChargebackMap() {
		return positiveChargebackMap();
	}
}
