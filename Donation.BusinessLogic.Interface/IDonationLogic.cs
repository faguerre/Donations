using System;
using System.Collections.Generic;
using Donation.Domain;

namespace Donation.BusinessLogic.Interface
{
     public interface IDonationLogic
    {
        Result<Domain.Donation> Add(Domain.Donation donation);
        Result<Domain.Donation> ExtendDonation(int id);
        Result<Domain.Donation> ConfirmDonation(int idDonation, string tokenUser);
        Result<Domain.Donation> Delete(int id);
        Result<Domain.Donation> Get(int id);
        IEnumerable<Domain.Donation> GetAll();
        Result<DonationTag> AddTag(int donationId, List<String> donationTags);
        IEnumerable<Domain.Donation> DonationsForUserTags(Guid token);
        IEnumerable<Domain.Donation> GetDonationsConfirmed(Guid token);
        IEnumerable<Domain.Donation> GetDonationsCreatedByUser(string token, bool active);
    }
}
