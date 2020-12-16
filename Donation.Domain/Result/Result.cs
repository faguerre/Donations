using System;

namespace Donation.Domain
{
    public class Result <T> where T : class
    {
        public string Message {get; set;}
        public T ResultObject {get; set;}
        public Result()
        {
            ResultObject = null;
            Message = "";
        }
    }
}
