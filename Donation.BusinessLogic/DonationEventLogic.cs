using System.Collections.Generic;
using Donation.BusinessLogic.Interface;
using Donation.DataAccess.Interface;
using Donation.Domain;

namespace Donation.BusinessLogic
{
    public class DonationEventLogic : IDonationEventLogic
    {
        private IUnitOfWork unitOfWork;
        public DonationEventLogic(IUnitOfWork _unitOfWork)
        {
            unitOfWork = _unitOfWork;
        }

        public Result<DonationEvent> Add(DonationEvent donationEvent)
        {
            if (unitOfWork.DonationEventRepository.Exist(x => x.Name.ToUpper().Equals(donationEvent.Name.ToUpper())))
            {
                return new Result<DonationEvent>()
                {
                    Message = "The event name already exists."
                };
            }
            else
            {
                User user = unitOfWork.UserRepository.Get(x => x.Email.ToUpper().Equals(donationEvent.CreatorUser.Email.ToUpper()));
                donationEvent.CreatorUser = user;
                unitOfWork.DonationEventRepository.Add(donationEvent);
                unitOfWork.DonationEventRepository.Save();
                return new Result<DonationEvent>()
                {
                    ResultObject = unitOfWork.DonationEventRepository.Get(x => x.Name.ToUpper().Equals(donationEvent.Name.ToUpper())),
                    //donationEvent,
                    Message = "Success confirmation"
                };
            }
        }

        public Result<DonationEvent> Delete(int id)
        {
            if (unitOfWork.DonationEventRepository.Exist(x => x.Id == id))
            {
                DonationEvent donationEvent = unitOfWork.DonationEventRepository.Get(x => x.Id == id);
                donationEvent.Active = false;
                unitOfWork.DonationEventRepository.Save();
                return new Result<DonationEvent>()
                {
                    ResultObject = new DonationEvent(),
                    Message = "The donation event has been removed."
                };
            }
            else
            {
                return new Result<DonationEvent>()
                {
                    Message = "The event does not exist."
                };
            }
        }

        public Result<DonationEvent> Get(int id)
        {
            if (unitOfWork.DonationEventRepository.Exist(x => x.Id == id))
            {
                return new Result<DonationEvent>()
                {
                    ResultObject = unitOfWork.DonationEventRepository.Get(x => x.Id == id)
                };
            }
            else
            {
                return new Result<DonationEvent>()
                {
                    Message = "The event does not exist."
                };
            }
        }


        public IEnumerable<DonationEvent> GetAll()
        {
            return unitOfWork.DonationEventRepository.GetAll();
        }
    }
}