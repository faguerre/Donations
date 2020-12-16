using System;
using Donation.DataAccess.Interface;
using Donation.Domain;
using  System.Collections.Generic;
using  System.Linq;
using  Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class DonationTagRepository : IRepository<DonationTag>
    {
        protected  DbContext  Context {get; set;}

         public DonationTagRepository(DbContext  context)
		{
			Context = context;
		}

        public DonationTag Get(Func<DonationTag, bool> predicate)
		{
			DonationTag donationTagToreturn = null;
			donationTagToreturn = Context.Set<DonationTag>().FirstOrDefault(predicate);
			return donationTagToreturn;
		}

		public IEnumerable<DonationTag> GetAll()
		{
            IEnumerable<DonationTag> returnList = Context.Set<DonationTag>().ToList();
            return returnList;
		}

		public bool Exist(Func<DonationTag, bool> predicate)
		{
			DonationTag donationTag = null;
			donationTag = Context.Set<DonationTag>().FirstOrDefault(predicate);
            if (donationTag == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        public void Add(DonationTag entity) 
		{
			Context.Set<DonationTag>().Add(entity);
		}

        public void Delete(int id) 
		{
			DonationTag donationTagtoDelete = null;
			donationTagtoDelete = Context.Set<DonationTag>().FirstOrDefault(x => x.DonationId == id);
			if(donationTagtoDelete != null) {
				Context.Set<DonationTag>().Remove(donationTagtoDelete);
			}
		}

		public void Update(int id, DonationTag entity) 
		{ 
			throw new NotImplementedException();
		}
        public void Save() 
		{
            Context.SaveChanges();
		}
 
    }
}