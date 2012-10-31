class Login < ActiveRecord::Base
  attr_accessible :acceskey, :channelid, :ipadress, :port, :state
end
