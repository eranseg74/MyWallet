
package com.sgl.mywallet;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class AccountTypes
{
  private Double posInterest;
  private String ownerId;
  private Double negInterest;
  private Date created;
  private String type;
  private String objectId;
  private Date updated;

  public AccountTypes(String type, Double posInterest, Double negInterest) {
    this.posInterest = posInterest;
    this.negInterest = negInterest;
    this.type = type;
  }

  public AccountTypes() {
    this.posInterest = 0.0;
    this.negInterest = 0.0;
    this.type = "Regular";
  }

  public Double getPosInterest()
  {
    return posInterest;
  }

  public void setPosInterest( Double posInterest )
  {
    this.posInterest = posInterest;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Double getNegInterest()
  {
    return negInterest;
  }

  public void setNegInterest( Double negInterest )
  {
    this.negInterest = negInterest;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public Date getUpdated()
  {
    return updated;
  }

                                                    
  public AccountTypes save()
  {
    return Backendless.Data.of( AccountTypes.class ).save( this );
  }

  public void saveAsync( AsyncCallback<AccountTypes> callback )
  {
    Backendless.Data.of( AccountTypes.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( AccountTypes.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( AccountTypes.class ).remove( this, callback );
  }

  public static AccountTypes findById( String id )
  {
    return Backendless.Data.of( AccountTypes.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<AccountTypes> callback )
  {
    Backendless.Data.of( AccountTypes.class ).findById( id, callback );
  }

  public static AccountTypes findFirst()
  {
    return Backendless.Data.of( AccountTypes.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<AccountTypes> callback )
  {
    Backendless.Data.of( AccountTypes.class ).findFirst( callback );
  }

  public static AccountTypes findLast()
  {
    return Backendless.Data.of( AccountTypes.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<AccountTypes> callback )
  {
    Backendless.Data.of( AccountTypes.class ).findLast( callback );
  }

  public static List<AccountTypes> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( AccountTypes.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<AccountTypes>> callback )
  {
    Backendless.Data.of( AccountTypes.class ).find( queryBuilder, callback );
  }
}