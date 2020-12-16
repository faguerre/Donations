using System;
using Donation.DataAccess.Interface;
using Donation.Domain;
using  System.Collections.Generic;
using  System.Linq;
using  Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class SessionRepository : IRepository<Session>
    {
        protected  DbContext  Context {get; set;}

         public SessionRepository(DbContext  context)
		{
			Context = context;
		}

        public void Add(Session entity) 
		{
			Context.Set<Session>().Add(entity);
		}

		public Session Get(Func<Session, bool> predicate)
		{
			Session sessionToreturn = null;
			sessionToreturn = Context.Set<Session>().FirstOrDefault(predicate);
			return sessionToreturn;
		}

		public IEnumerable<Session> GetAll()
		{
            IEnumerable<Session> returnList = Context.Set<Session>().ToList();
            return returnList;
		}
		
		public void Delete(int id) 
		{
			Session sessiontoDelete = null;
			sessiontoDelete = Context.Set<Session>().FirstOrDefault(x => x.Id == id);
			if(sessiontoDelete != null) {
				Context.Set<Session>().Remove(sessiontoDelete);
			}
		}

		public void Update(int id, Session entity) 
		{ 
			Session session = Context.Set<Session>().Where(x => x.Id == id).FirstOrDefault();
            session.Token = entity.Token;
            session.CreateDate = DateTime.Now;
		}

		public bool Exist(Func<Session, bool> predicate)
		{
			Session session = null;
			session = Context.Set<Session>().FirstOrDefault(predicate);
            if (session == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
 
		public void Save() 
		{
			Context.SaveChanges();
		}
    }
}