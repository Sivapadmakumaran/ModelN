package pages;

import org.openqa.selenium.By;

/**
 * UI Map for Common Objects
 */
public class Common {

	public By link,labelValue,textbox,button,navmenuItem,navMenuBar,dropdown,lookupBtn,tab;
	public By dialogTextBox,dialogDropDown,checkbox,multipleDropdown,leftMenuLink,radioButton,splitSecTableRowByIndex,dialogButton;
	public By label,toolMenuBtn,tableStatus,tableExceptionCheckbox,tableTextArea,tableSelect;
	public By splitFirstTableHeaderFilter,splitSecTableHeaderFilter,typeAheadLabel,tableCheckBox,tableFilterRow;
	public By tableDropDownByIndex,tableReportLink,multipleTextBox,selectNextRowInProductsTable,ndcExpiryDateField,ndcExpiryDateFieldCheckbox,clickLinkinTable;
	public By partialLabel,iconinTableHeader,editIconinTable,imgButton,footerButton,tableClaimLink,tableMenuItem,tableMenu,splitSecTableWithMultiFilterRowByIndex;
	public By multiRadioButton,textArea,tableColumnSortAsc,tableColumnSortDesc,tableColumnName,editbutton,revisionHistoryTable,tableTextInput,tableExceptionsCheckbox;
	
	
	public static final By userName =  By.xpath("//*[normalize-space(@class)='lbWrapper']//a[contains(@name,'userMenu')]");
	public static final By nextPage = By.xpath(".//a[@title='Go to the next page in this table']");
	public static final By tableHeader = By.xpath("//table[contains(@class,'splitControl')]//tr[contains(@class,'tableRow headerRow')]//td[contains(@style,'width:')]");
	public static final By splitFirstTableHeader = By.xpath("//div[@class='horizontalSubTables']//table[contains(@class,'Header')]//thead//tr//td[contains(@style,'width')]");
	public static final By dialogTableHeader = By.xpath(".//table[contains(@class,'splitControlHeaderTable') and not(@disabled='true')]//thead//tr//td");
	public static final By dialogTableRow = By.xpath("(.//table[contains(@class,'splitControlHeaderTable') and not(@disabled='true')]//tbody//tr)|(.//table[contains(@class,'splitControlBodyTable')]//tbody//tr)");
	
	public static final By splitSecTableHeader = By.xpath("//div[contains(@class,'splitControl')]//table[contains(@class,'splitControlHeaderTable')]//thead/tr/td[contains(@style,'width')]");
	public static final By tableRow = By.xpath("//table[contains(@class,'splitControl')]//tr[contains(@class,'tableRow bodyRow')]");
	public static final By splitFirstTableRow = By.xpath("//div[@class='horizontalSubTables']//table[contains(@class,'BodyTable') ]//tbody//tr");
	public static final By splitSecTableRow = By.xpath("//div[contains(@class,'Scroll')]//table//tbody//tr");
	public static final By splitFirstTableRowWithMultiFilter = By.xpath("//div[@class='horizontalSubTables']//table[contains(@class,'splitControlHeaderTable')]//tbody//tr[contains(@style,'height')]");
	public static final By splitSecTableRowWithMultiFilter = By.xpath("//div[contains(@class,'splitControl')]//table[contains(@class,'splitControlHeaderTable')]//tbody//tr[contains(@style,'height')]");
	//
	public static final By selectFirstRowInTable= By.xpath("//div[contains(@class,'splitControl')]//table//tr[1]//a");
	public static final By documentFrame = By.xpath("//iframe[@id='documentIFrame']");
	public static final By dynFrame = By.xpath("//iframe[@class='dynContainer' and contains(@style,'visible')]");
	public static final By refresh= By.xpath("//div[contains(@comppath,'mainMenu')]//following-sibling::div//a[contains(@name,'browserControls-refresh') and contains(@comppath, 'browserControls.refresh')]");
    public static final By jobalert= By.xpath("//div[contains(@class,'CMnAlertBarLayout-showAlert')]");
    public static final By filterButton= By.xpath("(//a[contains(@name,'findButton')])|(.//*[@value='Filter'])");
    public static final By clearFilterButton= By.xpath("//a[contains(@name,'clearButton')]");
    public static final By acceptOverrideBtn= By.xpath("//a[contains(@name,'acceptOverrideResults-opener')]");
    public static final By loadingMsg= By.xpath("(.//span[@id='loadingMsg' and contains(@style,'visibility: visible')])|(//div[contains(@class,'spinner_container')])|(//*[contains(@class,'spinner_large')])");
    public static final By open= By.xpath("//span[contains(@id,'chargebackLines-OpenClosed') and (contains(@title,'Open'))]");
    public static final By close= By.xpath("//a[contains(@id,'chargebackLines-OpenClosed') and (contains(@title,'Closed'))]");
    public static final By directOpen= By.xpath("//span[contains(@id,'DirectLines-OpenClosed') and (contains(@title,'Open'))]");
    public static final By directClose= By.xpath("//a[contains(@id,'DirectLines-OpenClosed') and (contains(@title,'Closed'))]");
    public static final By additem= By.xpath("//button[contains(@id,'STRUCTDOCPK') and @title='Add' and @type='button']");
    public static final By searchitem= By.xpath("//button[text()='Search' and @type='button']");
    public static final By lifeCycleStatus= By.xpath("//div[@data-column-attr='LifecycleStatus' and contains(text(),'Closed')]");
    public static final By lineRefNum= By.xpath(".//a[@data-column-attr='LineRefNum']");
    public static final By rightMenu= By.xpath("(.//a[contains(@comppath,'mainMenu.rightMenu.opener')]|//*[contains(@id,'suiteDropDownMenu')]//a[contains(@class,'ddMenuItemLink')])");
    public static final By externalFrame= By.xpath(".//iframe[@class='CMnExternalFileComp']");
    public static final By externalTableHeader= By.xpath(".//table[contains(@style,'border-collapse') and @class='ls']//td[@type='columnTitle']");
    public static final By externalTableRow= By.xpath(".//table[contains(@style,'border-collapse') and @class='ls']//tr");
    public static final By ndcTableRow = By.xpath("//table[contains(@id,'htmlTable-1-0')]/tbody/tr");
    public static final By okButton =By.xpath("//a[contains(@comppath,'dialogFrame.btnOk')]");
    public static final By ndcProductinFirstRow =By.xpath("//table[contains(@id,'htmlTable-1-0')]//tr[1]/td[2]/span");
    public static final By ndcProductinSecondRow =By.xpath("//table[contains(@id,'htmlTable-1-0')]//tr[2]/td[2]/span");
    public static final By uraCalculationJobStatus =By.xpath("//div[contains(@class,'splitControl')]//td[4]/span[contains(@comppath,'searcher.viewContainer.commands')]");
    public static final By fileUpload =By.xpath("//input[contains(@name,'fileUpload')]");
    public static final By chooseFile =By.xpath("//input[contains(@name,'file')]");
    public static final By dialogRefreshBtn =By.xpath("(//a[contains(@comppath,'btnRefresh')])|(.//button[@title='Refresh' and @type='button'])");
    public static final By xmlTextArea =By.xpath("//div[@id='frameContent']//textarea");
    public static final By tableCheckbox =By.xpath("(//div[contains(@class,'vScroll')]//tr[1]//td)[1]//input[not(@type='hidden')]");
    public static final By reportCheckbox =By.xpath("//div[contains(@class,'CMnMcdClaimReportTableComp')]//tr//td//input[@type='checkbox']");
    public static final By reportSection = By.xpath("//*[contains(@class,'title') and (contains(text(),'Reports'))]");
    public static final By searchValueTextbox = By.xpath("//input[@name='searchValue']");
    public static final By transactionFilebutton = By.xpath(".//input[@value='Transaction Files']");
    public static final By validateButton = By.xpath(".//*[@value='Validate']");
    public static final By transactionFileTableStatus = By.xpath("//table[@class='containerWidth']//*[@altid='Status' and @title]");
    public static final By submissionId = By.xpath("//span[contains(@comppath,'submissionNumComp')]");
    public static final By analystLookup = By.xpath("//label//text()[normalize-space()='Analyst:']//ancestor::td//following-sibling::td//a[contains(@comppath,'analyst')]");
    public static final By rebateTableStatus = By.xpath("//table[contains(@class,'splitControlHeaderTable tableElement')]//select[contains(@comppath,'rebatesTableComp.HEADER-1-4')]");
    public static final By rebateSelectAllCheckbox = By.xpath("//*[contains(@name,'selectAllCheckBox') and contains(@comppath,'selectAllCheckBox')]");
    public static final By dueAmountField = By.xpath("//span[contains(@comppath,'rebatesTableComp.FOOTER-0-6')]");
    public static final By rebateAmountDue = By.xpath("//table[contains(@class,'splitControlHeaderTable tableElement')]//select[contains(@comppath,'rebatesTableComp.HEADER-1-6.op')]");
    public static final By rebateAmountDueValue = By.xpath("//*[contains(@comppath,'rebatesTableComp.HEADER-1-6.op')]//following-sibling::input");
    public static final By netPaymentAmount = By.xpath("//span[contains(@comppath,'mcoPaymentPackage.tabs.msg')]");
    public static final By alertOk = By.xpath("//div[contains(@class,'CMnPopupComp CMnContainerComp') and contains(@style, 'visible')]//div[contains(@class,'dialogFrameButtons')]//a[contains(@name,'btnOk')]");
    public static final By alertYes = By.xpath("//div[contains(@class,'CMnPopupComp CMnContainerComp') and contains(@style, 'visible')]//div[contains(@class,'dialogFrameButtons')]//a[contains(@name,'btnYes')]");
    public static final By getEdiJobTime =By.xpath("//span[text()='Recently Viewed Documents']//following::table[contains(@class,'splitControlBodyTable tableElement')]//tr[1]//td[2]//span");
    public static final By navigateToJobFromEdiJobTime =By.xpath("//span[text()='Recently Viewed Documents']//following::table[contains(@class,'splitControlBodyTable tableElement')]//tr[1]//td[1]//a");
	public static final By plainSplitSecTableHeader = By.xpath("//div[contains(@class,'splitControl')]//table[contains(@class,'splitControlHeaderTable')]//thead/tr/td");
    public static final By plainSplitFirstTableHeader = By.xpath("//div[@class='horizontalSubTables']//table[contains(@class,'Header')]//thead//tr//td");
	public static final By edi849 = By.xpath("//table[contains(@class,'splitControlBodyTable tableElement')]//td//span[contains(text(),'Scheduled')]//parent::td//parent::tr//a[text()[normalize-space()='Publish Paid Payments']]");
	public static final By tableLookUpBtn = By.xpath(".//tr//child::td[contains(@class,'layoutForceElement')]//following-sibling::td//a[contains(@class,'editButton')]");
	public static final By reportView = By.xpath("//td[text()[normalize-space()='View']]");
	public static final By productDropdown = By.xpath("//select[contains(@name,'2Selector')]");
	public static final By sessionDropdown = By.xpath("//select[contains(@name,'operatorSelector')]");
	public static final By dateInput = By.xpath("//input[contains(@name,'editorComp')]");
	public static final By tickButton = By.xpath("//a[contains(@comppath,'okButton')]");
	
	public Common(String strLabel) {
		
		link = By.xpath("(.//*[text()='"+strLabel+"']//ancestor::tr[contains(@class,'bodyRow ')]//a)|(.//li//a[normalize-space(text())='"+strLabel+"'])|(.//div[contains(@comppath,'"+strLabel+"') and @style='display:inline;']//a[contains(@comppath,'"+strLabel+"') and contains(@name,'"+strLabel+"')])");
		labelValue = By.xpath("(.//*[normalize-space(text())='"+strLabel+"']//ancestor::tr[contains(@style,'border-style')]//div[contains(@class,'labeledComp')])|(.//label[normalize-space(text())='"+strLabel+"']//following-sibling::div/p)|(//label[contains(text(),'" + strLabel + "')]/../following-sibling::td//span)|(//span[normalize-space(text())='" + strLabel + "']/..//div/a)");
		textbox = By.xpath("(.//*[normalize-space(text())='"+strLabel+"']//ancestor::div[contains(@class,'labeledGroup')]//input[not(@type='hidden')])|(//input[@placeholder='"+strLabel+"'])|(//*[contains(@class,'control_header') and contains(text(),'" + strLabel + "')]//following-sibling::td//input)|(//text()[normalize-space()='" + strLabel + "']//preceding-sibling::input[@type='text'])|(.//*[normalize-space(text())='" + strLabel + "']//ancestor::tr[contains(@class,'tableRow')]//input[contains(@class,'textInput')])");
		/*button = By.xpath("(.//*[normalize-space(text())='"+strLabel+"'])|(.//a[@title='"+strLabel+"' and contains(@class,'Button')])");*/
		button = By.xpath("(.//*[normalize-space(text())='"+strLabel+"'])|(.//a[@title='"+strLabel+"' and contains(@class,'Button')])|(.//button[@title='"+strLabel+"' and @type='button'])|(.//*[text()[normalize-space()='" + strLabel + "']])|(.//*[@value='" + strLabel + "'])");
		radioButton = By.xpath("(.//label[normalize-space(text())='"+strLabel+"']//parent::div//input[@type='radio'])|(//*[contains(text(),'"+strLabel+"')]//preceding-sibling::td/input)|(//input[@type='radio' and @p_name='" + strLabel + "'])|(//text()[normalize-space()='" + strLabel + "']//parent::td//input[@type='radio'  and not(contains(@checked,'checked'))])");
		navmenuItem = By.xpath("(.//div[contains(@class,'menu') and contains(@style,'visible')]//a[text()='" + strLabel + "']|//*[contains(@id,'suiteDropDownMenu')]//a[contains(text(),'"+strLabel+"')])");
		navMenuBar = By.xpath("(.//a[normalize-space(text())='" + strLabel + "' and contains(@name,'navMenuBar')]//ancestor::div[contains(@class,'NavMenuButton')]//a[contains(@name,'opener')])|(.//*[text()[normalize-space()='" + strLabel + "'] and contains(@name,'navMenuBar') and contains(@class,'overflowOpener')])");
		tableMenuItem = By.xpath(".//table[@role='menuitem']//td[text()='"+strLabel+"']");
		tableMenu = By.xpath(".//table[@role='button' and @class='menuItem_normal']//img[contains(@title,'"+strLabel+"')]");
		dropdown = By.xpath("(.//*[normalize-space(text())='" + strLabel + "']//ancestor::div[contains(@class,'labeledGroup') or contains(@class,'PagingInfo')]//select)|(.//*[normalize-space(text())='" + strLabel + "']//ancestor::tr[contains(@class,'tableRow')]//select)");		
		lookupBtn = By.xpath(".//*[normalize-space(text())='" + strLabel + "']//ancestor::div[contains(@class,'PanelLayout') or @class='labeledGroup']//a");
		splitSecTableRowByIndex = By.xpath("(//div[contains(@class,'Scroll')]//table//tbody//tr)[" + strLabel + "]");
		splitSecTableWithMultiFilterRowByIndex = By.xpath("(//div[contains(@class,'splitControl')]//table[contains(@class,'splitControlHeaderTable')]//tbody//tr[contains(@style,'height')])[" + strLabel + "]");
		label= By.xpath(".//*[normalize-space(text())='"+strLabel+"']");
		partialLabel= By.xpath(".//*[contains(text(),'"+strLabel+"')]");
		toolMenuBtn= By.xpath(".//*[text()[normalize-space()='" + strLabel + "']]//ancestor::div[contains(@class,'MenuButton')]//button[contains(@class,'opener')]");
		tableStatus= By.xpath("//td[@title='Sort By: "+strLabel+"']");
        tableExceptionsCheckbox= By.xpath("(//span[@title='Exceptions']//parent::td/parent::tr)["+strLabel+"]//td//input[@type='checkbox']");
        tableExceptionCheckbox= By.xpath("(//span[@title='Exception']//parent::td/parent::tr)["+strLabel+"]//td//input[@type='checkbox']");
        tableTextArea= By.xpath("//table[contains(@class,'splitControlHeaderTable')]//tr[contains(@class,'bodyRow-"+strLabel+"')]//td//textarea");
        tableSelect= By.xpath("//table[contains(@class,'splitControlHeaderTable')]//tr[contains(@class,'bodyRow-"+strLabel+"')]//td//select");
        tableTextInput=By.xpath("//table[contains(@class,'splitControlHeaderTable')]//tr[contains(@class,'bodyRow-"+strLabel+"')]//td//input[@type='text']");
        splitFirstTableHeaderFilter = By.xpath("(//div[@class='horizontalSubTables']//table[contains(@class,'Header')]//thead//tr[2]//td)[" + strLabel + "]");
    	splitSecTableHeaderFilter = By.xpath("(//div[contains(@class,'splitControl')]//table[contains(@class,'splitControlHeaderTable')]//thead/tr[2]/td)[" + strLabel + "]");
    	typeAheadLabel=By.xpath("//span[contains(@id,'label') and @title='" + strLabel + "']");
    	tab = By.xpath(".//*[normalize-space(text())='" + strLabel + "' and contains(@class,'Tab')]");
    	tableCheckBox = By.xpath("(.//*[normalize-space(text())='" + strLabel + "']//ancestor::tr[contains(@class,'tableRow')]//td//input[@type='checkbox'])|(.//*[text()='" + strLabel + "']//ancestor::tr[contains(@class,'tableRow')]//td//input[@type='checkbox'])");
    	tableFilterRow = By.xpath("//table[contains(@class,'Header')]//thead//tr[2]//td[" + strLabel + "]");
    	selectNextRowInProductsTable = By.xpath("//div[contains(@class,'CMnTableControlBarComp')]//following-sibling::div//table//tr["+ strLabel +"]//a[not(contains(@style,'white-space'))]");
    	ndcExpiryDateField = By.xpath("//table[contains(@id,'htmlTable-1-1')]//tr[" + strLabel + "]/td[4]/span");
    	ndcExpiryDateFieldCheckbox = By.xpath("//table[contains(@id,'htmlTable-1-0')]//tr[" + strLabel + "]/td[1]/input[@type='checkbox']");
    	tableReportLink = By.xpath("//div[contains(@class,'CMnMcdClaimReportTableComp')]//tr[1]//td[2]//a[contains(text(),'" + strLabel + "')]");
    	checkbox = By.xpath("(.//*[normalize-space(text())='" + strLabel + "']//ancestor::div[contains(@class,'labeledComp')]//input[@type='checkbox'])|(//*[contains(text(),'" + strLabel + "')]//following-sibling::td//input)|(//*[normalize-space(text())='" + strLabel + "']//ancestor::tr//input[@type='checkbox' and @name='selectedFiles'])|(//input[@type='checkbox' and contains(@name,'" + strLabel + "')])");
    	iconinTableHeader= By.xpath("//*[contains(@title,'" + strLabel + "')]//parent::a");
    	editIconinTable= By.xpath("//*[contains(@class,'control_header') and contains(text(),'" + strLabel + "')]//following-sibling::td//img");
    	tableClaimLink=By.xpath("//td//span[contains(text(),'" + strLabel + "')]//../..//a");
    	imgButton = By.xpath(".//a[@title='" + strLabel + "' and contains(@name,'browserControls')]");
    	footerButton = By.xpath(".//footer//*[normalize-space(text())='" + strLabel + "']");
    	textArea = By.xpath("(.//label[normalize-space(text())='" + strLabel + "']//parent::div//textarea)|(.//label[normalize-space(text())='Reason:']/../following-sibling::td//textarea)");
    	tableColumnSortAsc =  By.xpath(".//*[contains(@comppath,'HEADER') and normalize-space(text())='" + strLabel + "']//parent::td//a[not(@class='inactiveSortComp') and contains(@name,'ascending')]");
        tableColumnSortDesc =  By.xpath(".//*[contains(@comppath,'HEADER') and normalize-space(text())='" + strLabel + "']//parent::td//a[contains(@name,'descending')]");
        tableColumnName = By.xpath(".//*[contains(@comppath,'HEADER') and normalize-space(text())='" + strLabel + "']");
        editbutton= By.xpath("//*[contains(text(),'" + strLabel + "')]/ancestor::div[@class='mainSection CMnLinearLayout CMnBaseLayout']//a[contains(@name,'editButton')]");
	}
	public Common(String strLabel1, String strLabel2)  {
		dialogTextBox = By.xpath("//span[@class='title' and text()='" + strLabel1 + "']//ancestor::form//label[normalize-space(text())='" + strLabel2 + "']//parent::td//following-sibling::td//input[not(@type='hidden')]");
		dialogDropDown = By.xpath("//span[@class='title' and text()='" + strLabel1 + "']//ancestor::form//label[normalize-space(text())='" + strLabel2 + "']//parent::td//following-sibling::td//select");
		leftMenuLink = By.xpath(".//*[text()='" + strLabel1 + "']//ancestor::div[contains(@class,'NavSectionItems') or contains(@class,'menuPane')]//a[text()='" + strLabel2 + "']");
		multipleDropdown = By.xpath(".//*[normalize-space(text())='" + strLabel1 + "']//ancestor::div[contains(@class,'labeledGroup')]//select[contains(@name,'" + strLabel2 + "')]");
		checkbox = By.xpath(".//*[normalize-space(text())='" + strLabel1 + "']//ancestor::div[@class='labeledGroup']//label[text()='" + strLabel2 + "']//parent::div//input[@type='checkbox']");
		multiRadioButton = By.xpath(".//*[text()='" + strLabel1 + "']//ancestor::div[@class='labeledGroup' or @class='frameContent']//input[@type='radio' and @value='" + strLabel2 + "']");
		dialogButton = By.xpath("(.//span[normalize-space(@class='title') and text()='" + strLabel1 + "']//ancestor::form//a[contains(@comppath,'" + strLabel2 + "') and contains(@name,'dialog')])|(.//span[@id='title' and text()='" + strLabel1 + "']//ancestor::div[contains(@class,'Popup') and contains(@style,'visible')]//a[contains(@comppath,'" + strLabel2 + "')])");
		multipleTextBox = By.xpath(".//*[normalize-space(text())='" + strLabel1 + "']//ancestor::div[contains(@class,'labeledGroup')]//input[@type='text' and contains(@name,'" + strLabel2 + "')]");
		clickLinkinTable = By.xpath("//*[contains(text(),'" + strLabel1 + "') and not (@disabled)]//parent::td//parent::tr//a[text()[normalize-space()='" + strLabel2 + "']]");
	    revisionHistoryTable=  By.xpath("//span[normalize-space(text())='" + strLabel1 + "']//ancestor::div[@class='vSplitOnlyHeaderDiv']/following-sibling::div//td/span[normalize-space(text())='" + strLabel2 + "']");
	}

}
