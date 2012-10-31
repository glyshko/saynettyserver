class Host < ActiveRecord::Base
  attr_accessible :acceskey, :active, :name
#  belongs_to :events, primary_key => "acceskey"
end
