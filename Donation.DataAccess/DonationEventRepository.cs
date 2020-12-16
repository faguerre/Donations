using System;
using System.Collections.Generic;
using System.Linq;
using Donation.DataAccess.Interface;
using Donation.Domain;
using Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class DonationEventRepository : IRepository<DonationEvent>
    {
        protected DbContext Context { get; set; }

        public DonationEventRepository(DbContext context)
        {
            Context = context;
        }

        public void Add(DonationEvent entity)
        {
            Context.Set<DonationEvent>().Add(entity);
        }

        public void Delete(int id)
        {
            DonationEvent donationEventtoDelete = null;
            donationEventtoDelete = Context.Set<DonationEvent>().FirstOrDefault(x => x.Id == id);
            if (donationEventtoDelete != null)
            {
                Context.Set<DonationEvent>().Remove(donationEventtoDelete);
            }
        }

        public bool Exist(Func<DonationEvent, bool> predicate)
        {
            DonationEvent donationEvent = null;
            donationEvent = Context.Set<DonationEvent>().FirstOrDefault(predicate);
            return (donationEvent != null);
        }

        public DonationEvent Get(Func<DonationEvent, bool> predicate)
        {
            return Context.Set<DonationEvent>().FirstOrDefault(predicate);
        }

        public IEnumerable<DonationEvent> GetAll()
        {
            IEnumerable<DonationEvent> returnList = Context.Set<DonationEvent>().Where<DonationEvent>(x =>  x.Active==true).ToList(); 
//            IEnumerable<DonationEvent> returnList = Context.Set<DonationEvent>().Where<DonationEvent>(x => x.IsActive()==true).ToList(); 
            
            return returnList;
        }

        public void Save()
        {
            Context.SaveChanges();
        }

        public void Update(int id, DonationEvent entity)
        {
            DonationEvent donationEvent = Context.Set<DonationEvent>().Where(x => x.Id == id).FirstOrDefault();
            donationEvent.Active = entity.Active;
        }
    }
}