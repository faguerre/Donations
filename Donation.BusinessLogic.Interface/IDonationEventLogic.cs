using System.Collections.Generic;
using Donation.Domain;

namespace Donation.BusinessLogic.Interface
{
    public interface IDonationEventLogic
    {
        Result<DonationEvent> Add(DonationEvent donationEvent);
        Result<DonationEvent> Delete(int id);
        Result<DonationEvent> Get(int id);
        IEnumerable<DonationEvent> GetAll();
    }
}