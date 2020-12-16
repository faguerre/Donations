using System;
using Donation.DataAccess.Interface;
using Donation.Domain;
using  System.Collections.Generic;
using  System.Linq;
using  Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class UserTagRepository : IRepository<UserTag>
    {
        protected  DbContext  Context {get; set;}

         public UserTagRepository(DbContext  context)
		{
			Context = context;
		}

        public UserTag Get(Func<UserTag, bool> predicate)
		{
			UserTag userTagToreturn = null;
			userTagToreturn = Context.Set<UserTag>().FirstOrDefault(predicate);
			return userTagToreturn;
		}

		public IEnumerable<UserTag> GetAll()
		{
            IEnumerable<UserTag> returnList = Context.Set<UserTag>().ToList();
            return returnList;
		}

		public bool Exist(Func<UserTag, bool> predicate)
		{
			UserTag userTag = null;
			userTag = Context.Set<UserTag>().FirstOrDefault(predicate);
            if (userTag == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        public void Add(UserTag entity) 
		{
			Context.Set<UserTag>().Add(entity);
		}

        public void Delete(int id) 
		{
			UserTag userTagtoDelete = null;
			userTagtoDelete = Context.Set<UserTag>().FirstOrDefault(x => x.UserId == id);
			if(userTagtoDelete != null) {
				Context.Set<UserTag>().Remove(userTagtoDelete);
			}
		}

		public void Update(int id, UserTag entity) 
		{ 
			throw new NotImplementedException();
		}
        public void Save() 
		{
            Context.SaveChanges();
		}
 
    }
}