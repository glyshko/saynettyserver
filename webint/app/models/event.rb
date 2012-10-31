class Event < ActiveRecord::Base
  attr_accessible :channelid, :command, :status
  has_many :host, :foreign_key => "acceskey"
  has_many :login, :foreign_key => "acceskey"
end
