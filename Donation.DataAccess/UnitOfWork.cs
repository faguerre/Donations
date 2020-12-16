using System;
using Donation.DataAccess.Interface;
using Donation.Domain;
using Microsoft.EntityFrameworkCore;

namespace Donation.DataAccess
{
    public class UnitOfWork : IUnitOfWork
    {
        private readonly DbContext dbContext;

        public UnitOfWork(DbContext context)
        {
            dbContext = context;
        }

        private IRepository<User> userRepository;
        private IRepository<Domain.Donation> donationRepository;
        private IRepository<Session> sessionRepository;
        private IRepository<DonationEvent> donationEventRepository;

        private IRepository<Tag> tagRepository;
        private IRepository<UserTag> userTagRepository;
        private IRepository<DonationTag> donationTagRepository;
         
        public IRepository<User> UserRepository
        {
            get
            {
                if (userRepository == null)
                {
                    userRepository = new UserRepository(dbContext);
                }

                return userRepository;
            }
        }
        public IRepository<Domain.Donation> DonationRepository
        {
            get
            {
                if (donationRepository == null)
                {
                    donationRepository = new DonationRepository(dbContext);
                }

                return donationRepository;
            }
        }

        public IRepository<Session> SessionRepository
        {
            get
            {
                if (sessionRepository == null)
                {
                    sessionRepository = new SessionRepository(dbContext);
                }

                return sessionRepository;
            }
        }

        public IRepository<DonationEvent> DonationEventRepository
        {
            get
            {
                if (donationEventRepository == null)
                {
                    donationEventRepository = new DonationEventRepository(dbContext);
                }

                return donationEventRepository;
            }
        }
        public IRepository<Tag> TagRepository
        {
            get
            {
                if (tagRepository == null)
                {
                    tagRepository = new TagRepository(dbContext);
                }

                return tagRepository;
            }
        }

        public IRepository<UserTag> UserTagRepository
        {
            get
            {
                if (userTagRepository == null)
                {
                    userTagRepository = new UserTagRepository(dbContext);
                }

                return userTagRepository;
            }
        }

        public IRepository<DonationTag> DonationTagRepository
        {
            get
            {
                if (donationTagRepository == null)
                {
                    donationTagRepository = new DonationTagRepository(dbContext);
                }

                return donationTagRepository;
            }
        }
    }
}
