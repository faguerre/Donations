using System;
using Donation.Domain;

namespace Donation.DataAccess.Interface
{
    public interface IUnitOfWork
    {
        IRepository<User> UserRepository {get;}
        IRepository<Domain.Donation> DonationRepository {get;}
        IRepository<Session> SessionRepository {get;}
        IRepository<DonationEvent> DonationEventRepository {get;}
        IRepository<Tag> TagRepository {get;}
        IRepository<UserTag> UserTagRepository {get;}
        IRepository<DonationTag> DonationTagRepository {get;}
    }
}
