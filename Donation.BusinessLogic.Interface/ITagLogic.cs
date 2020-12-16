using System;
using System.Collections.Generic;
using Donation.Domain;

namespace Donation.BusinessLogic.Interface
{
     public interface ITagLogic
    {
        Result<Tag> Get(int id);
        IEnumerable<Tag> GetAll();
    }
}
