using System;
using Donation.DataAccess.Interface;
using Donation.Domain;
using  System.Collections.Generic;
using  System.Linq;
using  Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class TagRepository : IRepository<Tag>
    {
        protected  DbContext  Context {get; set;}

         public TagRepository(DbContext  context)
		{
			Context = context;
		}

        public Tag Get(Func<Tag, bool> predicate)
		{
			Tag tagToreturn = null;
			tagToreturn = Context.Set<Tag>().FirstOrDefault(predicate);
			return tagToreturn;
		}

		public IEnumerable<Tag> GetAll()
		{
            IEnumerable<Tag> returnList = Context.Set<Tag>().ToList();
            return returnList;
		}

		public bool Exist(Func<Tag, bool> predicate)
		{
			Tag tag = null;
			tag = Context.Set<Tag>().FirstOrDefault(predicate);
            if (tag == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        public void Add(Tag entity) 
		{
			throw new NotImplementedException();
		}

        public void Delete(int id) 
		{
			throw new NotImplementedException();
		}

		public void Update(int id, Tag entity) 
		{ 
			throw new NotImplementedException();
		}
        public void Save() 
		{
            throw new NotImplementedException();
		}
 
    }
}