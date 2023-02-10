# Loan

GET http://localhost:8080/engine-rest/engine/myengine/process-definition

POST http://localhost:8080/engine-rest/engine/myengine/process-definition/loanProcess:2:16/start

	{"variables":
		{"amount" : {"value" : 1321, "type": "long"},
		 "clientId" : {"value" : "gClient", "type": "string"}
		},
		"businessKey" : "myBusinessKey"
    }




    hWndListBox = CreateWindowEx(
        NULL, 
        "LISTBOX", 
        NULL, 
        WS_BORDER | WS_CHILD | WS_VISIBLE | ES_AUTOVSCROLL | LBS_NOTIFY, 
        50, 35, 200, 100, 
        hWnd,
        (HMENU) IDC_LISTBOX, GetModuleHandle(NULL), NULL);


    hWndConnectBtn = CreateWindowEx(
        NULL, 
        "BUTTON", 
        "OK", 
        WS_TABSTOP | WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON, // | WS_DISABLED, 
        50, 220, 100, 24, hWnd, (HMENU)IDC_CONNECT_BUTTON, GetModuleHandle(NULL), NULL);

    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)"my Host1 user1323212");
    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)"my Host2 user2453345345345");
    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)"my Host3 user3");
    
        case WM_COMMAND:    
        switch (HIWORD(wParam))
        {
        case LBN_SELCHANGE:
        {
            //EnableWindow( GetDlgItem( hWnd, (HMENU)IDC_BUTTON_MAIN ), TRUE );
            EnableWindow(hWndConnectBtn, true);
            break;
        }
        }
        switch (LOWORD(wParam))
        {
        //case IDC_LISTBOX:
        //    MessageBox(hWnd, _T("Hello listbox"), _T("hust a listbox"), MB_OK);
        //    break;
        case IDC_CONNECT_BUTTON:
        {
            int index = SendMessage(hWndListBox, LB_GETCURSEL, 0, 0);
            if (index != LB_ERR) {
                //length = SendMessage(hWndListL, LB_GETTEXTLEN, NULL, NULL);
                SendMessage(hWndListBox, LB_GETTEXT, index, (LPARAM)buffer);
                MessageBox(hWnd, buffer, _T("just a listbox"), MB_OK);
                //SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)buffer);
                //SendMessage(hWndListBox, LB_DELETESTRING, NULL, NULL);
                //EnableWindow(hWndConnectBtn, false);
            }
            break;
        }
        }
        break;
