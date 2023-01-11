# Loan

GET http://localhost:8080/engine-rest/engine/myengine/process-definition

POST http://localhost:8080/engine-rest/engine/myengine/process-definition/loanProcess:2:16/start

	{"variables":
		{"amount" : {"value" : 1321, "type": "long"},
		 "clientId" : {"value" : "gClient", "type": "string"}
		},
		"businessKey" : "myBusinessKey"
    }


#pragma once
#include <Windows.h>
#include <CommCtrl.h>
#include <tchar.h>
//#include "resource.h"
#include <atlbase.h>


#pragma comment(linker, \
  "\"/manifestdependency:type='Win32' "\
  "name='Microsoft.Windows.Common-Controls' "\
  "version='6.0.0.0' "\
  "processorArchitecture='*' "\
  "publicKeyToken='6595b64144ccf1df' "\
  "language='*'\"")

#pragma comment(lib, "ComCtl32.lib")
#import "C:\\Windows\\System32\\mstscax.dll"
using namespace MSTSCLib;

class CTscEventSink : public IMsTscAxEvents
{
public:
    CTscEventSink()
    {
        _ulRefs = 1;
        m_dwEvtCookie = 0;
    }

    ~CTscEventSink()
    {
    }

    BOOL    Attach(IMsTscAx* pTscAx)
    {
        CComPtr<IConnectionPointContainer>    cpCPCont;
        HRESULT hr;

        hr = pTscAx->QueryInterface(IID_IConnectionPointContainer, (LPVOID*)&cpCPCont);
        if (FAILED(hr))
            return FALSE;

        hr = cpCPCont->FindConnectionPoint(__uuidof(IMsTscAxEvents), &m_cpConnect);
        if (FAILED(hr))
        {
            return FALSE;
        }

        hr = m_cpConnect->Advise(this, &m_dwEvtCookie);
        if (FAILED(hr))
        {
            m_dwEvtCookie = 0;
            return FALSE;
        }

        return TRUE;
    }

    void    Detach()
    {
        if ((m_dwEvtCookie) && (m_cpConnect))
            m_cpConnect->Unadvise(m_dwEvtCookie);
        if (m_cpConnect)
            m_cpConnect = NULL;
    }

    //IUnknown Methods
    STDMETHOD(QueryInterface) (REFIID riid, LPVOID* ppv)
    {
        if (riid == __uuidof(IMsTscAxEvents))
        {
            *ppv = (IMsTscAxEvents*)this;
        }
        else
        {
            *ppv = NULL;
            return E_NOINTERFACE;
        }
        AddRef();
        return S_OK;
    }
    STDMETHOD_(ULONG, AddRef) (void)
    {
        InterlockedIncrement((LONG*)&_ulRefs);
        return _ulRefs;
    }
    STDMETHOD_(ULONG, Release) (void)
    {
        ULONG ulRefs = _ulRefs;
        if (InterlockedDecrement((LONG*)&_ulRefs) == 0)
        {
            delete this;
            return 0;
        }
        return _ulRefs;
    }

    HRESULT OnLoginComplete()
    {
        ::MessageBoxA(NULL, "Login complete", "", 0);
    }

    HRESULT OnLogonError(
        long lError)
    {
        ::MessageBoxA(NULL, "OnLogonError", "", 0);
    }
    HRESULT OnDisconnected(
        long discReason)
    {
        ::MessageBoxA(NULL, "OnDisconnected", "", 0);
    }
    //IDispatch Methods

    STDMETHOD(GetTypeInfoCount)(UINT FAR* pctinfo)
    {
        return E_NOTIMPL;
    }

    STDMETHOD(GetTypeInfo)(UINT itinfo, LCID lcid, ITypeInfo FAR* FAR* pptinfo)
    {
        return E_NOTIMPL;
    }

    STDMETHOD(GetIDsOfNames)(REFIID riid, OLECHAR FAR* FAR* rgszNames, UINT cNames,
        LCID lcid, DISPID FAR* rgdispid)
    {
        return E_NOTIMPL;
    }

    STDMETHOD(Invoke)(DISPID dispidMember, REFIID riid, LCID lcid, WORD wFlags,
        DISPPARAMS FAR* pdispparams, VARIANT FAR* pvarResult,
        EXCEPINFO FAR* pexcepinfo, UINT FAR* puArgErr)
    {
        //AfxTrace(_T("Invoke!\n"));
        return S_OK;
    }

protected:
    ULONG        _ulRefs;

    DWORD                                m_dwEvtCookie;
    CComPtr<IConnectionPoint>    m_cpConnect;

};

CTscEventSink* pCTscEventSink;
MSTSCLib::IMsRdpClient5* pInterface = NULL;

void RdpConnect()
{
    HRESULT hrInit = CoInitialize(NULL);
    if (FAILED(hrInit)) return;

    CLSID clsid = __uuidof(MSTSCLib::MsRdpClient5);
    IID iid = __uuidof(MSTSCLib::IMsRdpClient5);

    HRESULT hrInterface = CoCreateInstance(clsid, NULL, CLSCTX_INPROC_SERVER, iid, (void**)&pInterface);


    if (SUCCEEDED(hrInterface))
    {
        pCTscEventSink = new CTscEventSink();
        BOOL b = pCTscEventSink->Attach(pInterface);

        int iScreenWidth = GetSystemMetrics(SM_CXSCREEN);
        int iScreenHeight = GetSystemMetrics(SM_CYSCREEN);

        pInterface->PutServer(L"127.0.0.1");
        pInterface->PutFullScreen(true);
        pInterface->PutDesktopWidth(iScreenWidth);
        pInterface->PutDesktopHeight(iScreenHeight);
        pInterface->PutUserName(L"admin");
        pInterface->AdvancedSettings2->PutClearTextPassword(L"12345");


        HRESULT hrConnect = pInterface->Connect();
        if (FAILED(hrConnect))
        {
            MessageBoxW(NULL, L"pInterface->Connect()", L"Error", 0);
        }
        else MessageBoxW(NULL, L"pInterface->Connect() Success!!!", L"Success", 0);

    }
    else
    {
        wchar_t buf[16] = { 0 };
        _ltow(hrInterface, buf, 16);
        MessageBoxW(NULL, buf, L"CoCreateInstance failed", 0);
    }

}
