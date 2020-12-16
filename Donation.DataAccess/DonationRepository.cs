using System;
using Donation.DataAccess.Interface;
using Donation.Domain;
using  System.Collections.Generic;
using  System.Linq;
using  Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class DonationRepository : IRepository<Domain.Donation>
    {
        protected  DbContext  Context {get; set;}

         public DonationRepository(DbContext  context)
		{
			Context = context;
		}

        public void Add(Domain.Donation entity) 
		{
			Context.Set<Domain.Donation>().Add(entity);
		}

		public Domain.Donation Get(Func<Domain.Donation, bool> predicate)
		{
			Domain.Donation donationToreturn = null;
			donationToreturn = Context.Set<Domain.Donation>().FirstOrDefault(predicate);
			return donationToreturn;
		}

		public IEnumerable<Domain.Donation> GetAll()
		{
            IEnumerable<Domain.Donation> returnList = Context.Set<Domain.Donation>().ToList();
			CalculateDaysLeft(returnList);
            return returnList;
		}

        private void CalculateDaysLeft(IEnumerable<Domain.Donation> returnList)
        {
           foreach (var item in returnList)
		   {
			   State state = item.State;
			   state.UpdateDaysLeft();
			   Context.SaveChanges();
		   }
        }

        public void Delete(int id) 
		{
			Domain.Donation donationtoDelete = null;
			donationtoDelete = Context.Set<Domain.Donation>().FirstOrDefault(x => x.Id == id);
			if(donationtoDelete != null) {
				RemoveListFiles(donationtoDelete);
				Context.Set<Domain.Donation>().Remove(donationtoDelete);
			}
		}

        private void RemoveListFiles(Domain.Donation donationtoDelete)
        {
           for (int i = 0; i < donationtoDelete.Files.Count ; i++)
			{
					donationtoDelete.Files.RemoveAt(i);	
			}
			Context.SaveChanges();
        }

        public void Update(int id, Domain.Donation entity) 
		{ 
			Domain.Donation donation = Context.Set<Domain.Donation>().Where(x => x.Id == id).FirstOrDefault();
			donation.State.WasExtended = true;
            donation.State.FinishDate = donation.State.FinishDate.AddDays(15);
		}

		public bool Exist(Func<Domain.Donation, bool> predicate)
		{
			Domain.Donation donation = null;
			donation = Context.Set<Domain.Donation>().FirstOrDefault(predicate);
            if (donation == null)
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