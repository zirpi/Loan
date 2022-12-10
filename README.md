# Loan

GET http://localhost:8080/engine-rest/engine/myengine/process-definition

POST http://localhost:8080/engine-rest/engine/myengine/process-definition/loanProcess:2:16/start

	{"variables":
		{"amount" : {"value" : 1321, "type": "long"},
		 "clientId" : {"value" : "gClient", "type": "string"}
		},
		"businessKey" : "myBusinessKey"
    }







// Your project must be apartment threaded or the (AtlAx)CreateControl(Lic)(Ex)
// calls will fail.
#define _ATL_APARTMENT_THREADED
#include <atlbase.h>
#include <atlwin.h>
#include <atlhost.h>

#import "mstscax.dll" rename_namespace("mstsc") raw_interfaces_only no_implementation


//#import "PROGID:MSCAL.Calendar.7" no_namespace, raw_interfaces_only

// Child window class that will be subclassed for hosting Active X control
class CChildWindow : public CWindowImpl<CChildWindow> {
public:
    BEGIN_MSG_MAP(CChildWindow)
    END_MSG_MAP()
};

class CMainWindow : public CWindowImpl<CMainWindow, CWindow, CFrameWinTraits>,
    public IDispEventImpl<1, CMainWindow,
    &__uuidof(mstsc::IMsTscAxEvents),
    &__uuidof(mstsc::__MSTSCLib), 1, 0>
{

private:
    typedef IDispEventImpl<1, CMainWindow,
        &__uuidof(mstsc::IMsTscAxEvents),
        &__uuidof(mstsc::__MSTSCLib), 1, 0> RdpEventsSink;

public:

    CChildWindow m_wndChild;
    CAxWindow2 m_axwnd;
    CWindow m_wndEdit;

    CComPtr<mstsc::IMsRdpClient> client;
    CComPtr<mstsc::IMsRdpClientAdvancedSettings> client_settings;
    BSTR server_name = SysAllocString(L"happyhost"); // server_name(UTF8ToUTF16(server_endpoint_.ToStringWithoutPort()).c_str());

    static ATL::CWndClassInfo& GetWndClassInfo() {
        static ATL::CWndClassInfo wc = {
           {
              sizeof(WNDCLASSEX),
              CS_HREDRAW | CS_VREDRAW | CS_DBLCLKS,
              StartWindowProc,
              0, 0, NULL, NULL, NULL,
              (HBRUSH)(COLOR_WINDOW + 1),
              0,
              _T("MainWindow"),
              NULL
           },
           NULL, NULL, IDC_ARROW, TRUE, 0, _T("")
        };
        return wc;
    }

    BEGIN_MSG_MAP(CMainWindow)
        MESSAGE_HANDLER(WM_CREATE, OnCreate)
        MESSAGE_HANDLER(WM_DESTROY, OnDestroy)
    END_MSG_MAP()

    BEGIN_SINK_MAP(CMainWindow)
        SINK_ENTRY_EX(1, __uuidof(mstsc::IMsTscAxEvents), DISPID_CLICK, OnClick)
    END_SINK_MAP()

    // Helper to display events
    void DisplayNotification(TCHAR* pszMessage)
    {
        CWindow wnd;
        wnd.Attach(GetDlgItem(2));

        wnd.SendMessage(EM_SETSEL, (WPARAM)-1, -1);
        wnd.SendMessage(EM_REPLACESEL, 0, (LPARAM)pszMessage);
    }

    // Event Handler for Click
    STDMETHOD(OnClick)()
    {
        return S_OK;
    }

    LRESULT OnCreate(UINT, WPARAM, LPARAM, BOOL&) {
        HRESULT hr = E_INVALIDARG;

        _pAtlModule->Lock();

        RECT rect;
        GetClientRect(&rect);
        RECT rect2;
        rect2 = rect;
        rect2.bottom -= 100;

        m_axwnd.Create(m_hWnd, rect2, NULL, WS_CHILD | WS_VISIBLE | WS_BORDER, 0, 1);
        if (m_axwnd.m_hWnd != NULL)
        {
            CComPtr<IUnknown> spControl;

                hr = m_axwnd.CreateControlEx(
                    OLESTR("MsTscAx.MsTscAx"),
                    NULL,
                    NULL,
                    &spControl,
                    __uuidof(mstsc::IMsTscAxEvents),
                    reinterpret_cast<IUnknown*>(static_cast<RdpEventsSink*>(this)));


                if (FAILED(hr)) return -1; 
                
                hr = spControl.QueryInterface(&client);
                if (FAILED(hr)) return -2;

                hr = client->put_ColorDepth(32);
                if (FAILED(hr)) return -3;

                hr = client->put_DesktopWidth(100);
                if (FAILED(hr)) return -4;

                hr = client->put_DesktopHeight(100);
                if (FAILED(hr)) return -5;

                hr = client->put_Server(server_name);
                if (FAILED(hr)) return -6;

                // Fetch IMsRdpClientAdvancedSettings interface for the client.
                hr = client->get_AdvancedSettings2(&client_settings);
                if (FAILED(hr)) return -7;

                hr = client_settings->put_allowBackgroundInput(0);
                if (FAILED(hr)) return -8;

                hr = client_settings->put_BitmapPersistence(0);
                if (SUCCEEDED(hr))
                    hr = client_settings->put_CachePersistenceActive(0);
                if (FAILED(hr))  goto done;

                hr = client_settings->put_Compress(0);
                if (FAILED(hr))  goto done;

                hr = client_settings->put_DisableCtrlAltDel(0);
                if (FAILED(hr))  goto done;

                hr = client_settings->put_DisableRdpdr(FALSE);
                if (FAILED(hr))  goto done;

                hr = client_settings->put_DisplayConnectionBar(VARIANT_FALSE);
                if (FAILED(hr))  goto done;

                hr = client_settings->put_GrabFocusOnConnect(VARIANT_FALSE);
                if (FAILED(hr))  goto done;

                hr = client_settings->put_RDPPort(3389);
                if (FAILED(hr))  goto done;

                hr = client->Connect();
                if (FAILED(hr))  goto done;
        }

        //rect2 = rect;
        //rect2.top = rect.bottom - 100 + 1;
        //m_wndEdit.Create(_T("Edit"), m_hWnd, rect2, NULL, WS_CHILD | WS_VISIBLE |
        //    WS_BORDER | ES_AUTOHSCROLL | ES_AUTOVSCROLL | ES_MULTILINE, 0, 2);
        return 0;

    done:
        return -1;
    }

    LRESULT OnDestroy(UINT, WPARAM, LPARAM, BOOL&)
    {
        _pAtlModule->Unlock();
        return 0;
    }
};

class CHostActiveXModule : public CAtlExeModuleT<CHostActiveXModule> {
public:
    CMainWindow m_wndMain;

    HRESULT PreMessageLoop(int nCmdShow) {
        HRESULT hr = CAtlExeModuleT<CHostActiveXModule>::PreMessageLoop(nCmdShow);
        if (SUCCEEDED(hr)) {
            AtlAxWinInit();
            hr = S_OK;
            RECT rc;
            rc.top = rc.left = 100;
            rc.bottom = rc.right = 500;
            m_wndMain.Create(NULL, rc, _T("Host Calendar"));
            m_wndMain.ShowWindow(nCmdShow);
        }
        return hr;
    }

    HRESULT PostMessageLoop() {
        AtlAxWinTerm();
        return CAtlExeModuleT<CHostActiveXModule>::PostMessageLoop();
    }
};

CHostActiveXModule _AtlModule;

int APIENTRY _tWinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPTSTR lpCmdLine, int nCmdShow) {
    UNREFERENCED_PARAMETER(hInstance);
    UNREFERENCED_PARAMETER(hPrevInstance);

    return _AtlModule.WinMain(nCmdShow);
}
