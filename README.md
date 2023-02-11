# Loan

GET http://localhost:8080/engine-rest/engine/myengine/process-definition

POST http://localhost:8080/engine-rest/engine/myengine/process-definition/loanProcess:2:16/start

	{"variables":
		{"amount" : {"value" : 1321, "type": "long"},
		 "clientId" : {"value" : "gClient", "type": "string"}
		},
		"businessKey" : "myBusinessKey"
    }



    hWndShowDlgBtn = CreateWindowEx(
        NULL,
        "BUTTON",
        "Show Dialog",
        WS_TABSTOP | WS_VISIBLE | WS_CHILD | BS_DEFPUSHBUTTON, // | WS_DISABLED, 
        50, 320, 100, 24, hWnd, (HMENU)IDC_SHOWDLG_BUTTON, GetModuleHandle(NULL), NULL);


    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)"my Host1 user1323212");
    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)"my Host2 user2453345345345");
    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)"my Host3 user3");

    ShowWindow(hWnd, nCmdShow);
    UpdateWindow(hWnd);

    MSG msg;
    while (GetMessage(&msg, NULL, 0, 0)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    return (int)msg.wParam;

}


LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
    PAINTSTRUCT ps;
    HDC hdc;
    TCHAR greeting[] = _T("Hello, Windows desktop!");


    char buffer[50] = "dildirim";

    switch (message) {
    //case WM_CREATE:
    //case WM_PAINT:
    //    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM) "my Host1 user1");
    //    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM) "my Host2 user2");
    //    SendMessage(hWndListBox, LB_ADDSTRING, NULL, (LPARAM)"my Host3 user3");
    //    break;
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
        case IDC_SHOWDLG_BUTTON:
            //hwndDialog = CreateDialog(hInst, MAKEINTRESOURCE(IDD_DIALOG1), hWnd, (DLGPROC)GoToDialog);
            //ShowWindow(hwndDialog, SW_SHOW);
            int ret = DialogBox(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_DIALOG1), hWnd, GoToDialog);
            if (ret == IDCANCEL) {

            }
            else if (ret == IDOK) {

            }
            break;
        }
        break;

    case WM_DESTROY:
        PostQuitMessage(WM_QUIT);
        break;

    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
        break;
    }

    return 0;
}



void SetSplashImage(HWND hwndSplash, HBITMAP hbmpSplash)
{
    // get the size of the bitmap
    BITMAP bm;
    GetObject(hbmpSplash, sizeof(bm), &bm);
    SIZE sizeSplash = { bm.bmWidth, bm.bmHeight };

    // get the primary monitor's info
    POINT ptZero = { 0 };
    HMONITOR hmonPrimary = MonitorFromPoint(ptZero, MONITOR_DEFAULTTOPRIMARY);
    MONITORINFO monitorinfo = { 0 };
    monitorinfo.cbSize = sizeof(monitorinfo);
    GetMonitorInfo(hmonPrimary, &monitorinfo);

    // center the splash screen in the middle of the primary work area
    const RECT& rcWork = monitorinfo.rcWork;
    POINT ptOrigin;
    ptOrigin.x = 0;
    ptOrigin.y = rcWork.top + (rcWork.bottom - rcWork.top - sizeSplash.cy) / 2;

    // create a memory DC holding the splash bitmap
    HDC hdcScreen = GetDC(NULL);
    HDC hdcMem = CreateCompatibleDC(hdcScreen);
    HBITMAP hbmpOld = (HBITMAP)SelectObject(hdcMem, hbmpSplash);

    // use the source image's alpha channel for blending
    BLENDFUNCTION blend = { 0 };
    blend.BlendOp = AC_SRC_OVER;
    blend.SourceConstantAlpha = 255;
    blend.AlphaFormat = AC_SRC_ALPHA;

    // paint the window (in the right location) with the alpha-blended bitmap
    UpdateLayeredWindow(hwndSplash, hdcScreen, &ptOrigin, &sizeSplash,
        hdcMem, &ptZero, RGB(0, 0, 0), &blend, ULW_ALPHA);

    // delete temporary objects
    SelectObject(hdcMem, hbmpOld);
    DeleteDC(hdcMem);
    ReleaseDC(NULL, hdcScreen);
}


BOOL CALLBACK GoToDialog(HWND hwndDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
    switch (message)
    {
    case WM_INITDIALOG:
        MakeWindowTransparent(hwndDlg, 228);
        return TRUE;
    case WM_COMMAND:
        switch (LOWORD(wParam))
        {
        case IDOK:
            EndDialog(hwndDlg, IDOK);
            break;
        case IDCANCEL:
            EndDialog(hwndDlg, IDCANCEL);
            break;
        }
        break;
    default:
        return FALSE;
    }
    return TRUE;
}
