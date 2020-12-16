using System;
using System.Collections.Generic;

namespace Donation.DataAccess.Interface
{
    public interface IRepository<T> where T : class
    {
        void Add(T entity);
        IEnumerable<T> GetAll();
        T Get (Func<T, bool> predicate);
        void Delete(int id);
        void Update(int id, T entity);
        bool Exist(Func<T, bool> predicate);
        void Save();
    }
}
