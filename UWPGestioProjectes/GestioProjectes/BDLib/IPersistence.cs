namespace BDLib
{
    public interface IPersistence
    {
        void connect(string conexioString);
        void close();
        void commit();
        void rollback();

    }
}