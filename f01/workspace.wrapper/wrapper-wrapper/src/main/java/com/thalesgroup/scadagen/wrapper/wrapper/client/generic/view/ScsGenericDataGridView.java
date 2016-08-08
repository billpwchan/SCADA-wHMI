package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.view;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.GenericDataGridView;

/**
 * Generic Data Grid view.
 */
public class ScsGenericDataGridView extends GenericDataGridView {

    public void ackPage() {
//        DataGrid<EntityClient> dataGrid = getInnerDataGrid();
//        List<EntityClient> visibleEntityClient = dataGrid.getVisibleItems();
//        AlarmUtils.acknowledge(visibleEntityClient);
    }

    public void ackVisibleItems() {
        /*
         * RBI TESTS 31/10/2013
         * 
         * DataGrid<EntityClient> dataGrid = getInnerDataGrid();
         * 
         * List<EntityClient> entityInThePage = new ArrayList<EntityClient>();
         * List<EntityClient> visibleEntityClient = dataGrid.getVisibleItems();
         * 
         * 
         * int entityIndex = 0; for(EntityClient entity : visibleEntityClient){
         * int pixelOffsetTop =
         * dataGrid.getRowElement(entityIndex).getOffsetTop(); int scrollHeight
         * = dataGrid.getRowElement(entityIndex).getScrollHeight(); int
         * scrollTop = dataGrid.getRowElement(entityIndex).getScrollTop(); int
         * offsetHeight = dataGrid.getRowElement(entityIndex).getOffsetHeight();
         * int clientHeight =
         * dataGrid.getRowElement(entityIndex).getClientHeight();
         * 
         * 
         * int parentpixelOffsetTop = dataGrid.getElement().getOffsetTop(); int
         * parentscrollHeight = dataGrid.getElement().getScrollHeight(); int
         * parentscrollTop = dataGrid.getElement().getScrollTop(); int
         * parentoffsetHeight = dataGrid.getElement().getOffsetHeight(); int
         * datagridOffetHeight = dataGrid.getOffsetHeight(); int
         * dataGridClientHeight = dataGrid.getElement().getClientHeight();
         * 
         * 
         * int parentpixelOffsetTop2 =
         * dataGrid.getRowContainer().getOffsetTop(); int parentscrollHeight2 =
         * dataGrid.getRowContainer().getScrollHeight(); int parentscrollTop2 =
         * dataGrid.getRowContainer().getScrollTop(); int parentoffsetHeight2 =
         * dataGrid.getRowContainer().getOffsetHeight(); int
         * datagridOffetHeight2 = dataGrid.getRowContainer().getOffsetHeight();
         * int dataGridClientHeight2 =
         * dataGrid.getRowContainer().getClientHeight();
         * 
         * 
         * 
         * 
         * entityIndex++; }
         */
        /*
         * for(int i = 0; i < pageSize; i++){ EntityClient entity =
         * dataGrid.getVisibleItem(i); if(entity != null){
         * entityInThePage.add(entity); }else{ break; // end of the page } }
         * if(entityInThePage.size() > 0){
         * AlarmUtils.acknowledge(entityInThePage); }
         */
    }

    public static native void scrollToTop() /*-{ 
                                            $wnd.scrollTo(0, 0); 
                                            }-*/;
}
