using System;
using System.Collections.Generic;
using Donation.BusinessLogic.Interface;
using Donation.DataAccess.Interface;
using Donation.Domain;

namespace Donation.BusinessLogic
{
    public class DonationLogic : IDonationLogic
    {
    
    private IUnitOfWork unitOfWork;
    public DonationLogic (IUnitOfWork _unitOfWork) 
    {
        unitOfWork = _unitOfWork;
    }

        public Result<Domain.Donation> Add(Domain.Donation donation)
        {
            if(unitOfWork.UserRepository.Exist(x => x.Id == donation.CreatorUser.Id))
            {
                donation.CreatorUser = unitOfWork.UserRepository.Get(x => x.Id == donation.CreatorUser.Id);
                unitOfWork.DonationRepository.Add(donation);
                unitOfWork.DonationRepository.Save();
                return new Result<Domain.Donation> () {
                    ResultObject = donation
                };
            }else{
                return new Result<Domain.Donation> () {
                        Message = "The user selected as creator does not exist."
                };
            }
        }

        public Result<Domain.Donation> Delete(int id)
        {
           if(unitOfWork.DonationRepository.Exist(x =>x.Id == id)){
                unitOfWork.DonationRepository.Delete(id);
                unitOfWork.DonationRepository.Save();
                return new Result<Domain.Donation> () 
                {
                    ResultObject = new Domain.Donation(),
                    Message = "The donation "+ id + "has been removed."
                };
            }else {
                return new Result<Domain.Donation> () 
                {
                    Message = "It is not possible to delete the donation "+id+" because the id does not exist."
                };
            }
        }

        public Result<Domain.Donation> Get(int id)
        {
            if(unitOfWork.DonationRepository.Exist(x =>x.Id == id)){
                return new Result<Domain.Donation> () 
                {
                    ResultObject = unitOfWork.DonationRepository.Get(x => x.Id == id)
                };
            }else {
                return new Result<Domain.Donation> () 
                {
                    Message = "There is not donation related to the id "+ id
                };
            }
        }

        public IEnumerable<Domain.Donation> GetAll()
        {
            return DonationsAvailables(unitOfWork.DonationRepository.GetAll());
        }

        private IEnumerable<Domain.Donation> DonationsAvailables(IEnumerable<Domain.Donation> enumerable)
        {
            List<Domain.Donation> listFiltered = new List<Domain.Donation>();
            foreach (var item in enumerable)
            {
                if(item.State.ActualState != "Finished")
                {
                     NewMethod(listFiltered, item);
                }
            }
            return listFiltered;
        }

        public IEnumerable<Domain.Donation> GetDonationsConfirmed(Guid token) {
            List<Domain.Donation> listFiltered = new List<Domain.Donation>();
            IEnumerable<Domain.Donation> listDonations = unitOfWork.DonationRepository.GetAll();
            User userRelated = unitOfWork.SessionRepository.Get(x => x.Token == token).User;
            foreach (var item in listDonations)
            {
                if(item.ConfirmedUser != null && item.ConfirmedUser.Email.Equals(userRelated.Email)) 
                {
                    listFiltered.Add(item);
                }
            }
            return listFiltered;
        }

        public IEnumerable<Domain.Donation> GetDonationsCreatedByUser(string token, bool active)
        {
            List<Domain.Donation> listFiltered = new List<Domain.Donation>();
            IEnumerable<Domain.Donation> listDonations = unitOfWork.DonationRepository.GetAll();
            User userRelated = null;
            if(token != "")
            {
                userRelated = unitOfWork.SessionRepository.Get(x => x.Token == Guid.Parse(token)).User;
            }
            if (listDonations != null)
            {
                foreach (var item in listDonations)
                {
                    if (userRelated == null)
                    {
                        if (item.ConfirmedUser == null && item.State.ActualState != "Finished")
                        {
                            listFiltered.Add(item);
                        }
                    }
                    else
                    {
                        if (active)
                        {
                            if (item.CreatorUser.Email.Equals(userRelated.Email) && item.ConfirmedUser == null)
                            {
                                listFiltered.Add(item);
                            }
                        }
                        else
                        {
                            if (!item.CreatorUser.Email.Equals(userRelated.Email) && item.ConfirmedUser == null)
                            {
                                listFiltered.Add(item);
                            }
                        }
                    }

                }
            }
            return listFiltered;
        }

        private void NewMethod(List<Domain.Donation> listFiltered, Domain.Donation item)
        {
            if (item.ConfirmedUser == null)
            {
                if (item.State.WasExtended)
                {
                    if (item.State.CreationDate < item.State.CreationDate.AddDays(30))
                    {
                        listFiltered.Add(item);
                    }
                }
                else
                {
                    if (item.State.CreationDate < item.State.CreationDate.AddDays(15))
                    {
                        listFiltered.Add(item);
                    }
                }
            }
        }

        

        public Result<Domain.Donation> ExtendDonation(int id)
        {
            Domain.Donation _donation = unitOfWork.DonationRepository.Get(x => x.Id == id);
            
            if (_donation != null) 
            {
                if (_donation.ConfirmedUser == null) 
                {
                    if(!_donation.State.WasExtended) 
                    {
                        unitOfWork.DonationRepository.Update(id, _donation);
                        unitOfWork.DonationRepository.Save();
                        return new Result<Domain.Donation> () {
                            ResultObject = _donation,
                            Message = "The donation will be published 15 more days"
                        };
                    }else{
                        return new Result<Domain.Donation> () 
                        {
                            Message = "It is not possible to extend the donation 15 more day because it was previously extended."
                        };
                    }
                }
                else
                {
                    return new Result<Domain.Donation> () 
                    {
                        Message = "It is not possible to extend the donation because the donation was already confirmed."
                    };  
                }
            }
            else
            {
                return new Result<Domain.Donation> () 
                {
                   Message = "There is not donation related to the id "+ id
                };
            }
        }

        public Result<Domain.Donation> ConfirmDonation(int idDonation, string tokenRelated)
        {
            Domain.Donation _donation = unitOfWork.DonationRepository.Get(x => x.Id == idDonation);
            if (_donation != null) 
            {
                Session _activeSession = unitOfWork.SessionRepository.Get(x => x.Token.Equals(Guid.Parse(tokenRelated)));
                if(_activeSession.User != null)
                {
                    _donation.ConfirmedUser = _activeSession.User;
                    _donation.State.UpdateStatus();
                    unitOfWork.DonationRepository.Save();
                    return new Result<Domain.Donation> () {
                        ResultObject = _donation,
                        Message = "Success confirmation"
                    };
                }else{
                    return new Result<Domain.Donation> () 
                    {
                        Message = "There is not donation related to the id "+ _activeSession.User.Id
                    };
                }
            }
            else
            {
                return new Result<Domain.Donation> () 
                {
                   Message = "There is not donation related to the id "+ idDonation
                };
            }
        }

        public Result<DonationTag> AddTag(int donationId, List<String> donationTags)
        {
            for (int i = 0; i < donationTags.Count; i++)
            {
                DonationTag donationTag = new DonationTag();
                donationTag.DonationId = donationId;
                Tag tag = unitOfWork.TagRepository.Get(x => x.Name.Equals(donationTags[i]));
                donationTag.TagId = tag.Id;
                unitOfWork.DonationTagRepository.Add(donationTag);
                unitOfWork.DonationTagRepository.Save();
            }
            
            return new Result<DonationTag>()
            {
                Message = "Tags add to donation"
            };
        }

        public IEnumerable<Domain.Donation> DonationsForUserTags(Guid token)
        {
            List<Domain.Donation> listFiltered = new List<Domain.Donation>();
            Session _activeSession = unitOfWork.SessionRepository.Get(x => x.Token.Equals(token));
            List<UserTag> tags = _activeSession.User.userTag;
            List<int> userTags = new List<int>();
            foreach (var tag in tags)
            {
                userTags.Add(tag.TagId);
            }
            IEnumerable<Donation.Domain.Donation> donationList = unitOfWork.DonationRepository.GetAll();
            foreach (var item in donationList)
            {
                List<DonationTag> donationTags = item.DonationTag;
                foreach (var donTag in donationTags)
                {
                    if(userTags.Contains(donTag.Tag.Id)){
                        listFiltered.Add(item);
                    }
                }    
            }
            return listFiltered;
        }
    }
}