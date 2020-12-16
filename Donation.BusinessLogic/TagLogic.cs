using System.Collections.Generic;
using Donation.BusinessLogic.Interface;
using Donation.DataAccess.Interface;
using Donation.Domain;

namespace Donation.BusinessLogic
{
    public class TagLogic : ITagLogic
    {

        private IUnitOfWork unitOfWork;
        public TagLogic(IUnitOfWork _unitOfWork)
        {
            unitOfWork = _unitOfWork;
        }

        public Result<Tag> Get(int id)
        {
            if (unitOfWork.TagRepository.Exist(x => x.Id == id))
            {
                return new Result<Tag>()
                {
                    ResultObject = unitOfWork.TagRepository.Get(x => x.Id == id)
                };
            }
            else
            {
                return new Result<Tag>()
                {
                    Message = "There is not Tag related to the id " + id
                };
            }
        }

        public IEnumerable<Tag> GetAll()
        {
            return unitOfWork.TagRepository.GetAll();
        }
    }
}
