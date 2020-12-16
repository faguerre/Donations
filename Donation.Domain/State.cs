using System;

namespace Donation.Domain
{
    public class State
    {
        public int Id { get; set; }
        public string ActualState { get; set; }
        public DateTime CreationDate { get; set; }
        public DateTime FinishDate { get; set; }
        public bool WasExtended { get; set; }
        public int DaysLeft { get; set; }
        public State()
        {
            ActualState = "Created";
            CreationDate = System.DateTime.Today.Date;
            FinishDate = System.DateTime.Today.Date.AddDays(15);
            WasExtended = false;
            DaysLeft = 15;
        }

        public void UpdateStatus()
        {
            this.ActualState = "Finished";
            this.FinishDate = DateTime.Now;
        }

        public void UpdateDaysLeft()
        {
            DateTime today = DateTime.Now.Date;
            if (today == CreationDate.Date)
            {
                DaysLeft = 15;
            }
            if (today > CreationDate.Date)
            {
                DaysLeft = (FinishDate.Date - today).Days;
            }
        }
    }
}
