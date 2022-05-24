using System;


public interface IPersistence
{

    void connect(String conexioString);
    void close();
    void commit();
    void rollback();


}

