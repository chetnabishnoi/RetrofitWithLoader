package com.retrofit.service;


import android.content.Context;

import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RetrofitRestApi {

    private static final String JBASIC = "Basic ";
    private static RestAdapter mRestAdapter;
    private RetrofitService mRetrofitService;
    private Context mContext;

    public RetrofitRestApi(final Context context) {
        mContext = context;

        String baseUrl = mContext.getString(com.retrofit.R.string.domain);
        if (mRestAdapter == null) {
            OkHttpClient okHttpClient = new OkHttpClient();

            /*Include this line to support ssh*/
            // okHttpClient.setSslSocketFactory(getSslSocketFactory());

            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return true;

                }
            });

            File cacheDir = new File(context.getCacheDir(), context.getString(com.retrofit.R.string.cache_dir));

            try {
                HttpResponseCache cache = new HttpResponseCache(cacheDir, 1024);
                /*Include this line if your api supports caching*/
                //  okHttpClient.setResponseCache(cache);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            mRestAdapter = new RestAdapter.Builder().setServer(baseUrl).setLogLevel(RestAdapter.LogLevel.FULL)
                    /*Uncomment following code to support basic authentication or any other parameter that
                    should be included in every request. Like accept-language*/
                  /*  .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            requestFacade.addHeader(HEADER_AUTH, JBASIC + nawrasPreference.getBasicKey());
                        }
                    })*/
                    .setClient(new OkClient(okHttpClient))
                    .build();
        }
    }

    public RetrofitService getService() {
        if (mRetrofitService == null) {
            mRetrofitService = mRestAdapter.create(RetrofitService.class);
        }
        return mRetrofitService;
    }

    private SSLSocketFactory getSslSocketFactory() {
        final SSLSocketFactory delegate = getSSLContextWithCertificate().getSocketFactory();
        SSLSocketFactory socketFactory = new SSLSocketFactory() {

            @Override
            public String[] getDefaultCipherSuites() {
                return delegate.getDefaultCipherSuites();
            }

            @Override
            public String[] getSupportedCipherSuites() {
                return delegate.getSupportedCipherSuites();
            }

            @Override
            public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
                injectHostname(socket, s);
                return delegate.createSocket(socket, s, i, b);
            }

            @Override
            public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
                return delegate.createSocket(s, i);
            }

            @Override
            public Socket createSocket(String s, int i, InetAddress inetAddress, int i2) throws IOException, UnknownHostException {
                return delegate.createSocket(s, i, inetAddress, i2);
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
                return delegate.createSocket(inetAddress, i);
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
                return delegate.createSocket(inetAddress, i, inetAddress2, i2);
            }

            private void injectHostname(Socket socket, String host) {
                try {
                    Field field = InetAddress.class.getDeclaredField("hostName");
                    field.setAccessible(true);
                    field.set(socket.getInetAddress(), host);
                } catch (Exception ignored) {
                }
            }
        };

        return socketFactory;
    }

    private SSLContext getSSLContextWithCertificate() {
        /*Uncomment the following code to support CA certificate. Paste your certificate in the raw folder*/
      /*  try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = mContext.getResources().openRawResource(R.raw.selfcare_nawras_om);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            return context;
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
