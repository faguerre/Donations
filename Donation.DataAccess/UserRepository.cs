using System;
using Donation.DataAccess.Interface;
using Donation.Domain;
using  System.Collections.Generic;
using  System.Linq;
using  Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class UserRepository : IRepository<User>
    {
        protected  DbContext  Context {get; set;}

         public UserRepository(DbContext  context)
		{
			Context = context;
		}

        public void Add(User entity) 
		{
			Context.Set<User>().Add(entity);
		}

		public User Get(Func<User, bool> predicate)
		{
			User userToreturn = null;
			userToreturn = Context.Set<User>().FirstOrDefault(predicate);
			return userToreturn;
		}

		public IEnumerable<User> GetAll()
		{
            IEnumerable<User> returnList = Context.Set<User>().ToList();
            return returnList;
		}
		
		public void Delete(int id) 
		{
			User usertoDelete = null;
			usertoDelete = Context.Set<User>().FirstOrDefault(x => x.Id == id);
			if(usertoDelete != null) {
				Context.Set<User>().Remove(usertoDelete);
			}
		}

		public void Update(int id, User entity) 
		{ 
			User user = Context.Set<User>().Where(x => x.Id == id).FirstOrDefault();
			user.Name = entity.Name;
            user.Password = entity.Password;
			//user.Email = entity.Email;
            user.Phone = entity.Phone;
		}

		public bool Exist(Func<User, bool> predicate)
		{
			User user = null;
			user = Context.Set<User>().FirstOrDefault(predicate);
            if (user == null)
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