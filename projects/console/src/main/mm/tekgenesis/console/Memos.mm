package tekgenesis.console;

form MemosForm "Memos" on_load onLoad
{
    horizontal {
        clusterName "Cluster Name": String, display, label_col 4, col 4;
    };
    disable : Boolean, internal, default true;
    searchBox "Search" : text_field(255),optional, on_change filterMemos;

    memos : table(8),sortable {
        name "Name " : display;
        forceMemo : label, icon trash_o, tooltip "Force memo value recalculation", on_click forceMemo, hide when disable;
        seeMemo : label, icon eye, tooltip "See memo value", on_click seeMemoValue, hide when disable;
    };

    memoValueDialog "Memo": dialog
    {
         vertical, style "form-horizontal" {
            memoFqn "Memo" : String, display;
            memoValue "Current Value":text_area(20, 150), optional;
        };
        footer {
            closeSetDataDialog "Cancel" : button, on_click closeDialog,content_style "margin-left-10";
        };
    };
}